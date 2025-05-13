CREATE OR REPLACE PROCEDURE sp_save_invt(
    IN p_id_producto INTEGER,
    IN p_id_sucursal INTEGER,
    IN p_stock INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Verificar si ya existe el registro
    IF EXISTS (
        SELECT 1 FROM inventario
        WHERE id_producto = p_id_producto AND id_sucursal = p_id_sucursal
    ) THEN
        -- Si existe, actualiza el stock
        UPDATE inventario
        SET stock = p_stock
        WHERE id_producto = p_id_producto AND id_sucursal = p_id_sucursal;
    ELSE
        -- Si no existe, inserta un nuevo registro
        INSERT INTO inventario (id_producto, id_sucursal, stock)
        VALUES (p_id_producto, p_id_sucursal, p_stock);
    END IF;
END;
$$;



CREATE OR REPLACE PROCEDURE sp_eliminarInv(
    IN p_id_producto INTEGER,
    IN p_id_sucursal INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN

	delete from inventario
	where id_producto=p_id_producto and id_sucursal=p_id_sucursal;


END;
$$;


--funcion que obtine las transferencias seegun la sucursal
CREATE OR REPLACE FUNCTION fn_transPorSucursal(p_id_sucursal INTEGER)
RETURNS TABLE (
    idtransferencia INTEGER,
    monto INTEGER,
    fecha DATE,
    id_pedido INTEGER,
    comprobante VARCHAR,
    comentario VARCHAR,
    idestadotrnsf INTEGER
)
AS $$
BEGIN
    RETURN QUERY
    SELECT
        trn.idtransferencia,
        trn.monto,
        trn.fecha,
        trn.id_pedido,
        trn.comprobante,
        trn.comentario,
        trn.idestadotrnsf
    FROM transferencia trn
    JOIN pedido p ON p.id_pedido = trn.id_pedido
    JOIN sucursal suc ON suc.id_sucursal = p.id_sucursal
    WHERE suc.id_sucursal = p_id_sucursal;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM fn_transPorSucursal(1);


