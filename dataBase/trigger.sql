CREATE OR REPLACE FUNCTION fnActualizarStock()
RETURNS TRIGGER AS $$

BEGIN
    -- Verificar si es una inserción (INSERT)
    IF (TG_OP = 'INSERT') THEN
        -- Si es una inserción, restamos el stock del producto
        UPDATE inventario
        SET stock = stock - NEW.cantidad
        WHERE id_producto = NEW.id_producto
          AND id_sucursal = (
              SELECT id_sucursal
              FROM pedido
              WHERE id_pedido = NEW.id_pedido
          );

    -- Verificar si es una eliminación (DELETE)
    ELSIF (TG_OP = 'DELETE') THEN
        -- Si es una eliminación, restauramos el stock del producto
        UPDATE inventario
        SET stock = stock + OLD.cantidad
        WHERE id_producto = OLD.id_producto
          AND id_sucursal = (
              SELECT id_sucursal
              FROM pedido
              WHERE id_pedido = OLD.id_pedido
          );
    END IF;

    RETURN NULL;  -- No necesitamos devolver nada, solo actualizar el stock
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_actualizarStock
AFTER INSERT OR DELETE ON DetallePedido
FOR EACH ROW
EXECUTE FUNCTION fnActualizarStock();
