-- SECCION
INSERT INTO catservice.seccion (id, nombre, descripcion) VALUES (1, 'Seccion 1', 'Seccion 1');
INSERT INTO catservice.seccion (id, nombre, descripcion) VALUES (2, 'Seccion 2', 'Seccion 2');

-- SERVICIO
INSERT INTO catservice.servicio (id, nombre, abreviatura, contacto, seccion_id) VALUES (1, 'Servicio 1', 'Serv1', 'Nombre Apellidos', 1);
INSERT INTO catservice.servicio (id, nombre, abreviatura, contacto, seccion_id) VALUES (2, 'Servicio 2', 'Serv2', 'Nombre Apellidos', 1);

-- TIPO RESERVABLE 
INSERT INTO catservice.tipo_reservable (id, descripcion, duracion_min, dias_ante_max, dias_vista_max_calen, horas_ante_min, horas_ante_anular, estado, reserva_multi, servicio_id)
VALUES (1, 'TipoReservable1', 2, 3, 2, 4, 4, 'alta', false, 1);

INSERT INTO catservice.tipo_reservable (id, descripcion, duracion_min, dias_ante_max, dias_vista_max_calen, horas_ante_min, horas_ante_anular, estado, reserva_multi, servicio_id)
VALUES (2, 'TipoReservable2', 2, 3, 2, 4, 4, 'baja', true, 2);