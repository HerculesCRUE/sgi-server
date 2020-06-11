-- SECCION
INSERT INTO catservice.seccion (id, nombre, descripcion) VALUES (1, 'Seccion 1', 'Seccion 1');

-- SERVICIO
INSERT INTO catservice.servicio (id, nombre, abreviatura, contacto, seccion_id) VALUES (1, 'Servicio 1', 'Serv1', 'Nombre Apellidos', 1);

-- TIPO FUNGIBLE 
INSERT INTO catservice.tipo_fungible (id, nombre, servicio_id) VALUES (1, 'TipoFungible1', 1);