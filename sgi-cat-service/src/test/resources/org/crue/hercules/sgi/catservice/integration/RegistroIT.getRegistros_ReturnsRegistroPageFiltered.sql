-- SECCION
INSERT INTO catservice.seccion (id, nombre, descripcion) VALUES (1, 'Seccion 1', 'Seccion 1');
INSERT INTO catservice.seccion (id, nombre, descripcion) VALUES (2, 'Seccion 2', 'Seccion 2');

-- SERVICIO
INSERT INTO catservice.servicio (id, nombre, abreviatura, contacto, seccion_id) VALUES (1, 'Servicio 1', 'Serv1', 'Nombre Apellidos', 1);

-- REGISTRO
INSERT INTO catservice.registro (id, usuario_ref, servicio_id, estado_registro, entrega_papel, acepta_condiciones, observaciones) VALUES (1, 'user-998', 1, 'activo', true, true, 'regitro 1');
INSERT INTO catservice.registro (id, usuario_ref, servicio_id, estado_registro, entrega_papel, acepta_condiciones, observaciones) VALUES (2, 'user-558', 1, 'inactivo', true, true, 'regitro 2');
INSERT INTO catservice.registro (id, usuario_ref, servicio_id, estado_registro, entrega_papel, acepta_condiciones, observaciones) VALUES (3, 'user-996', 1, 'inactivo', true, true, 'regitro 3');