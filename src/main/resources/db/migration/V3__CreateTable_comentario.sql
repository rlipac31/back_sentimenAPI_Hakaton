-- 2. TABLA DE COMENTARIOS (CON CLASIFICACIÓN EMBEBIDA)

-- 2. TABLA DE COMENTARIOS (CON CLASIFICACIÓN EMBEBIDA)
CREATE TABLE comentarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    comentario TEXT NOT NULL,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Nuevo campo para almacenar el resultado del análisis
    tipo ENUM('positivo', 'negativo', 'neutro') NULL,

    -- **CORRECCIÓN AQUÍ:** Debe referenciar 'usuarios(id)', no 'usuarios(id_usuario)'
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE
);