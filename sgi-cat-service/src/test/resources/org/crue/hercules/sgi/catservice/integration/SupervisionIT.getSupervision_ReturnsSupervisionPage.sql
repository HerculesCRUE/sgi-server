-- SECCION
INSERT INTO catservice.seccion (id, nombre, descripcion) VALUES (1, 'Seccion 1', 'Seccion 1');

-- SERVICIO
INSERT INTO catservice.servicio (id, nombre, abreviatura, contacto, seccion_id) VALUES (1, 'Servicio 1', 'Serv1', 'Nombre Apellidos', 1);

-- SUPERVISION 
INSERT INTO catservice.supervision (id, usuario_ref, servicio_id) VALUES (1, 'UsuarioRef1', 1);
INSERT INTO catservice.supervision (id, usuario_ref, servicio_id) VALUES (2, 'UsuarioRef2', 1);
