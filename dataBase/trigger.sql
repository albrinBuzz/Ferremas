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
