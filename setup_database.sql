CREATE DATABASE IF NOT EXISTS domcar;
USE domcar;

CREATE TABLE IF NOT EXISTS viajes (
    codViaje INT AUTO_INCREMENT PRIMARY KEY,
    propietario VARCHAR(100) NOT NULL,
    ruta VARCHAR(200) NOT NULL,
    fechaSalida DATETIME NOT NULL,
    duracion INT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    plazasOfertadas INT NOT NULL,
    estadoViaje VARCHAR(50) NOT NULL DEFAULT 'ABIERTO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS reservas (
    codigoReserva VARCHAR(50) PRIMARY KEY,
    usuario VARCHAR(100) NOT NULL,
    plazasSolicitadas INT NOT NULL,
    fechaRealizacion DATETIME NOT NULL,
    viaje INT NOT NULL,
    FOREIGN KEY (viaje) REFERENCES viajes(codViaje) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO viajes (propietario, ruta, fechaSalida, duracion, precio, plazasOfertadas, estadoViaje) VALUES
('Juan Pérez', 'Madrid-Barcelona', '2027-12-15 10:00:00', 360, 35.50, 4, 'ABIERTO'),
('María García', 'Valencia-Alicante', '2027-12-16 14:30:00', 120, 15.00, 3, 'ABIERTO'),
('Carlos López', 'Sevilla-Málaga', '2027-12-17 09:00:00', 180, 25.00, 5, 'ABIERTO');
SELECT 'Base de datos creada exitosamente!' AS mensaje;
SELECT COUNT(*) AS total_viajes FROM viajes;
