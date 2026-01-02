CREATE OR REPLACE FUNCTION fnActualizarStock()
RETURNS TRIGGER AS $$

BEGIN

    IF ( TG_OP = 'INSERT') THEN

            UPDATE inventario
        SET stock = stock - NEW.cantidad
            WHERE  id_producto = NEW.id_producto
          AND id_sucursal = (
               SELECT id_sucursal
              FROM  pedido
              WHERE id_pedido = NEW.id_pedido
          );

    ELSIF (TG_OP = 'DELETE') THEN

            UPDATE inventario
        SET stock = stock + OLD.cantidad
        WHERE  id_producto = OLD.id_producto
          AND id_sucursal = (
              SELECT id_sucursal
                 FROM pedido
               WHERE id_pedido = OLD.id_pedido
          );
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_actualizarStock
AFTER INSERT  OR DELETE ON DetallePedido
FOR EACH ROW
EXECUTE FUNCTION fnActualizarStock();






CREATE OR REPLACE FUNCTION fn_notificar_cambio_pedido()
RETURNS TRIGGER AS $$
DECLARE
    v_nombre_estado VARCHAR(50);
    v_correo_destino VARCHAR(50);
BEGIN
    -- Solo actuar si el estado id_estadoPedido cambió
    IF (OLD.id_estadoPedido IS DISTINCT FROM NEW.id_estadoPedido) THEN
        -- 1. Obtener nombre del nuevo estado
        SELECT nombre INTO v_nombre_estado FROM EstadoPedido WHERE id_estadoPedido = NEW.id_estadoPedido;

        -- 2. Buscar correo (Primero en Usuarios, luego en Invitados)
        SELECT correo INTO v_correo_destino FROM USUARIO WHERE rut_usuario = NEW.rutCliente;
        IF v_correo_destino IS NULL THEN
            SELECT correo INTO v_correo_destino FROM ClienteInvitado WHERE rutCliente = NEW.rutCliente;
        END IF;

        -- 3. Insertar la notificación
        INSERT INTO Notificacion (rut_receptor, titulo, mensaje, tipo, id_referencia)
        VALUES (
            NEW.rutCliente,
            'Actualización de Pedido #' || NEW.id_pedido,
            'Su pedido ha cambiado al estado: ' || v_nombre_estado || '. Se ha notificado a: ' || COALESCE(v_correo_destino, 'correo no registrado'),
            'PEDIDO',
            NEW.id_pedido
        );
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_notificacion_pedido
AFTER UPDATE ON PEDIDO
FOR EACH ROW
EXECUTE FUNCTION fn_notificar_cambio_pedido();




ALTER TABLE Transferencia ADD COLUMN observacion_admin VARCHAR(255);

CREATE OR REPLACE FUNCTION fn_notificar_cambio_transferencia()
RETURNS TRIGGER AS $$
DECLARE
    v_rut_cliente VARCHAR(15);
    v_mensaje_detalle TEXT;
    v_titulo_dinamico VARCHAR(100);
    v_feedback_actual TEXT;
BEGIN
    -- 1. Detectar si hubo cambio de estado O si se agregó un nuevo feedback
    IF (OLD.idEstadoTrnsf IS DISTINCT FROM NEW.idEstadoTrnsf) OR
       (OLD.observacion_admin IS DISTINCT FROM NEW.observacion_admin) THEN

        -- Obtener RUT del cliente
        SELECT rutCliente INTO v_rut_cliente FROM PEDIDO WHERE id_pedido = NEW.id_pedido;

        -- 2. Lógica de Feedback: Solo capturamos el texto SI ES DISTINTO al anterior
        -- Esto evita que se reenvíe una observación vieja en cambios de estado posteriores
        IF (OLD.observacion_admin IS DISTINCT FROM NEW.observacion_admin) THEN
            v_feedback_actual := NEW.observacion_admin;
        ELSE
            v_feedback_actual := NULL; -- No hay feedback nuevo en esta actualización
        END IF;

        -- 3. Construcción del mensaje según el ID de estado
        CASE NEW.idEstadoTrnsf
            WHEN 2 THEN -- Rechazada
                v_titulo_dinamico := '⚠️ Acción Requerida: Pago Rechazado - Pedido #' || NEW.id_pedido;
                v_mensaje_detalle := 'Su transferencia ha sido rechazada. ' ||
                                     CASE
                                        WHEN v_feedback_actual IS NOT NULL THEN 'Motivo: "' || v_feedback_actual || '". '
                                        ELSE 'Por favor, revise que el monto y la imagen sean correctos. '
                                     END || 'Debe subir un nuevo comprobante.';

            WHEN 3 THEN -- Completada
                v_titulo_dinamico := '✅ Pago Validado con Éxito - Pedido #' || NEW.id_pedido;
                v_mensaje_detalle := 'Hemos validado su pago satisfactoriamente. ' ||
                                     COALESCE(v_feedback_actual, 'Su pedido ya está en proceso de preparación.') ||
                                     ' Pronto recibirá noticias sobre el despacho.';

            WHEN 4 THEN -- Cancelada
                v_titulo_dinamico := '🚫 Pago Cancelado - Pedido #' || NEW.id_pedido;
                v_mensaje_detalle := 'La transacción ha sido cancelada. ' || COALESCE(v_feedback_actual, '');

            ELSE
                v_titulo_dinamico := '🕒 Actualización de Pago - Pedido #' || NEW.id_pedido;
                v_mensaje_detalle := 'El estado de su pago ha cambiado a pendiente de revisión.';
        END CASE;

        -- 4. Insertar la notificación
        INSERT INTO Notificacion (rut_receptor, titulo, mensaje, tipo, id_referencia)
        VALUES (
            v_rut_cliente,
            v_titulo_dinamico,
            v_mensaje_detalle,
            'TRANSFERENCIA',
            NEW.id_pedido
        );

    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



CREATE TRIGGER tr_notificacion_transferencia
AFTER UPDATE ON Transferencia
FOR EACH ROW
EXECUTE FUNCTION fn_notificar_cambio_transferencia();


select * from notificacion;


select * from estadotransferencia;





-- Función con nombre profesional y descriptivo
CREATE OR REPLACE FUNCTION fn_notify_new_notification()
RETURNS TRIGGER AS
$$
BEGIN
    -- Emitimos el ID al canal 'canal_notificaciones'
    PERFORM pg_notify('canal_notificaciones', NEW.id_notificacion::TEXT);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Aplicación del trigger a la tabla Notificacion
DROP TRIGGER IF EXISTS tr_notify_new_notificacion ON Notificacion;

CREATE TRIGGER tr_notify_new_notificacion
AFTER INSERT ON Notificacion
FOR EACH ROW
EXECUTE FUNCTION fn_notify_new_notification();

