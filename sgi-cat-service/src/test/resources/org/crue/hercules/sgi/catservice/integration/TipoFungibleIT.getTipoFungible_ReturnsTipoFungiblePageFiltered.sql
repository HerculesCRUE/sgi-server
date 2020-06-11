-- SECCION
INSERT INTO catservice.seccion (id, nombre, descripcion) VALUES (1, 'Seccion 1', 'Seccion 1');
INSERT INTO catservice.seccion (id, nombre, descripcion) VALUES (2, 'Seccion 1', 'Seccion 1');

-- SERVICIO
INSERT INTO catservice.servicio (id, nombre, abreviatura, contacto, seccion_id) VALUES (1, 'Servicio 1', 'Serv1', 'Nombre Apellidos', 1);
INSERT INTO catservice.servicio (id, nombre, abreviatura, contacto, seccion_id) VALUES (2, 'Servicio 2', 'Serv2', 'Nombre Apellidos', 2);

-- TIPO FUNGIBLE
INSERT INTO catservice.tipo_fungible (id, nombre, servicio_id) VALUES (1, 'TipoFungible1', 1);
INSERT INTO catservice.tipo_fungible (id, nombre, servicio_id) VALUES (2, 'TipoFungible1', 1);
INSERT INTO catservice.tipo_fungible (id, nombre, servicio_id) VALUES (3, 'TipoFungible1', 1);
INSERT INTO catservice.tipo_fungible (id, nombre, servicio_id) VALUES (4, 'TipoFungible2', 2);
INSERT INTO catservice.tipo_fungible (id, nombre, servicio_id) VALUES (5, 'TipoFungible2', 2);