CREATE OR REPLACE FUNCTION fnActualizarStock()
RETURNS TRIGGER AS $$


BEGIN

	  UPDATE inventario
    SET stock = stock - NEW.cantidad
    WHERE id_producto = NEW.id_producto
      AND id_sucursal = (
          SELECT id_sucursal
          FROM pedido
          WHERE id_pedido = NEW.id_pedido
      );


    RETURN NEW;


END;
$$ LANGUAGE plpgsql;



CREATE TRIGGER tr_actualizarStock
AFTER INSERT ON DetallePedido
FOR EACH ROW
EXECUTE FUNCTION fnActualizarStock();
