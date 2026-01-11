CREATE TABLE usuarios
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    email          VARCHAR(100) UNIQUE,
    contrasenia    VARCHAR(100) NOT NULL,
    role           VARCHAR(20)  NOT NULL,
    fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP

);

alter table usuarios add state tinyint;
update usuarios set state=1;

-- 2. TABLA DE COMENTARIOS (CON CLASIFICACIÓN EMBEBIDA)
CREATE TABLE comentarios
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    comentario   TEXT NOT NULL,
    prevision    ENUM('POSITIVO', 'NEGATIVO', 'NEUTRO') NULL,
    provavilidad DECIMAL(2, 1) NULL,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

);

alter table comentarios add state tinyint;
update comentarios set state=1;