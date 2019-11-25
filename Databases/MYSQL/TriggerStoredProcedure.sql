DELIMITER $
CREATE TRIGGER `ORDER_TRIGGER`
BEFORE INSERT ON `ORDER`
FOR EACH ROW
BEGIN
	DECLARE PRICE FLOAT(7,2);
	DECLARE TEMP FLOAT(6,2);
	DECLARE POINTS INT;
	DECLARE Tmobo_size ENUM('uATX/MicroATX','ATX','ExtendedATX','MiniITX','SSI','Other');
	DECLARE Tcase_mobo_size SET('uATX/MicroATX','ATX','ExtendedATX','MiniITX','SSI','Other');
	DECLARE Tcpu_socket VARCHAR(20);
	DECLARE Tmobo_socket VARCHAR(20);
	DECLARE Tram_type ENUM('DDR3','DDR4','DDR3 SO-DIMM','DDR4 SO-DIMM');
	DECLARE Tmobo_ram_type ENUM('DDR3','DDR4','DDR3 SO-DIMM','DDR4 SO-DIMM');
	DECLARE Tcpu_tdp TINYINT UNSIGNED;
	DECLARE Tgpu_system_power SMALLINT UNSIGNED;
	DECLARE Tpsu_power SMALLINT UNSIGNED;
	DECLARE output ENUM('COMPABILITY SUCCESS','COMPABILITY FAILURE');

	IF NEW.used_together THEN
		SELECT mobo_size,mobo_socket,mobo_ram_type INTO Tmobo_size,Tmobo_socket,Tmobo_ram_type FROM `MOTHERBOARD`
		WHERE `MOTHERBOARD`.mobo_model=NEW.mobo_model;
		SELECT case_mobo_size INTO Tcase_mobo_size FROM `CASE`
		WHERE `CASE`.case_model=NEW.case_model;
		SELECT cpu_socket,cpu_tdp INTO Tcpu_socket,Tcpu_tdp FROM `CPU`
		WHERE `CPU`.cpu_model=NEW.cpu_model;
		SELECT ram_type INTO Tram_type FROM `RAM`
		WHERE `RAM`.ram_model=NEW.ram_model;
		SELECT gpu_system_power INTO Tgpu_system_power FROM `GPU`
		WHERE `GPU`.gpu_model=NEW.gpu_model;
		SELECT psu_power INTO Tpsu_power FROM `PSU`
		WHERE `PSU`.psu_model=NEW.psu_model;
		CALL `COMPABILITY` (Tmobo_size,Tcase_mobo_size,Tcpu_socket,Tmobo_socket,Tram_type,
							Tmobo_ram_type,Tcpu_tdp,Tgpu_system_power,Tpsu_power,output);
		IF output='COMPABILITY FAILURE' THEN
			SIGNAL SQLSTATE VALUE '45000'
				SET MESSAGE_TEXT='COMPABILITY FAILURE';
		END IF;
	END IF;

	SET PRICE=0;
	SET TEMP=0;
	IF NEW.cpu_model IS NOT NULL THEN
		SELECT cpu_price INTO TEMP FROM `CPU` WHERE cpu_model=NEW.cpu_model;
		SET PRICE=PRICE+TEMP;
	END IF;
	IF NEW.mobo_model IS NOT NULL THEN
		SELECT mobo_price INTO TEMP FROM `MOTHERBOARD` WHERE mobo_model=NEW.mobo_model;
		SET PRICE=PRICE+TEMP;
	END IF;
	IF NEW.gpu_model IS NOT NULL THEN
		SELECT gpu_price INTO TEMP FROM `GPU` WHERE gpu_model=NEW.gpu_model;
		SET PRICE=PRICE+TEMP;
	END IF;
	IF NEW.psu_model IS NOT NULL THEN
		SELECT psu_price INTO TEMP FROM `PSU` WHERE psu_model=NEW.psu_model;
		SET PRICE=PRICE+TEMP;
	END IF;
	IF NEW.ram_model IS NOT NULL THEN
		SELECT ram_price INTO TEMP FROM `RAM` WHERE ram_model=NEW.ram_model;
		SET PRICE=PRICE+TEMP;
	END IF;
	IF NEW.case_model IS NOT NULL THEN
		SELECT case_price INTO TEMP FROM `CASE` WHERE case_model=NEW.case_model;
		SET PRICE=PRICE+TEMP;
	END IF;
	IF NEW.ssd_model IS NOT NULL THEN
		SELECT ssd_price INTO TEMP FROM `SSD` WHERE ssd_model=NEW.ssd_model;
		SET PRICE=PRICE+TEMP;
	END IF;
	IF NEW.hdd_model IS NOT NULL THEN
		SELECT hdd_price INTO TEMP FROM `HDD` WHERE hdd_model=NEW.hdd_model;
		SET PRICE=PRICE+TEMP;
	END IF;
	IF NEW.extHD_model IS NOT NULL THEN
		SELECT exthd_price INTO TEMP FROM `extHD` WHERE exthd_model=NEW.extHD_model;
		SET PRICE=PRICE+TEMP;
	END IF;

	IF EXISTS (SELECT available_points FROM `CUSTOMER_CARD` WHERE available_points>=100&&`CUSTOMER_CARD`.customer_id=NEW.customer_id) THEN
		UPDATE `CUSTOMER_CARD`
		SET available_points=available_points-100
		WHERE available_points>=100&&`CUSTOMER_CARD`.customer_id=NEW.customer_id LIMIT 1;
		UPDATE `CUSTOMER_CARD`
		SET spent_points=spent_points+100
		WHERE available_points>=100&&`CUSTOMER_CARD`.customer_id=NEW.customer_id LIMIT 1;
		SET PRICE=PRICE-PRICE*0.1;
	END IF;

	UPDATE `CUSTOMER_CARD`
	SET available_points=available_points+PRICE*0.1
	WHERE `CUSTOMER_CARD`.customer_id=NEW.customer_id;

	SET NEW.price=PRICE;

END$

CREATE TRIGGER `COMPABILITY_TRIGGER`
AFTER INSERT ON `MOTHERBOARD`
FOR EACH ROW
BEGIN
	CALL `MATCH`(NEW.mobo_model);
END$
DELIMITER ;

DELIMITER $

CREATE PROCEDURE `MATCH`(IN mobo varchar(20))
BEGIN
	DECLARE TEMP VARCHAR(20);
	DECLARE FLAG TINYINT;
	DECLARE C CURSOR FOR 
		SELECT cpu_model FROM `CPU` INNER JOIN `MOTHERBOARD` ON cpu_socket=mobo_socket
		WHERE mobo=`MOTHERBOARD`.mobo_model;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET FLAG=1;
	SET FLAG=0;
	OPEN C;
	WHILE FLAG!=1 DO
		FETCH C INTO TEMP;
		IF FLAG!=1 THEN
			INSERT INTO `MODELS_COMPABILITY` VALUES(mobo,TEMP);
		END IF;
	END WHILE;

END$

CREATE PROCEDURE `COMPABILITY` (IN mobo_size ENUM('uATX/MicroATX','ATX','ExtendedATX','MiniITX','SSI','Other'),
								IN case_mobo_size SET('uATX/MicroATX','ATX','ExtendedATX','MiniITX','SSI','Other'),
								IN cpu_socket VARCHAR(20),IN mobo_socket VARCHAR(20),
								IN ram_type ENUM('DDR3','DDR4','DDR3 SO-DIMM','DDR4 SO-DIMM'),
								IN mobo_ram_type ENUM('DDR3','DDR4','DDR3 SO-DIMM','DDR4 SO-DIMM'),
								IN cpu_tdp TINYINT UNSIGNED,
								IN gpu_system_power SMALLINT UNSIGNED,
								IN psu_power SMALLINT UNSIGNED,
								OUT output ENUM('COMPABILITY SUCCESS','COMPABILITY FAILURE'))
BEGIN
	DECLARE RESULT BOOLEAN;
	SET RESULT= FIND_IN_SET(mobo_size,case_mobo_size) &&
				cpu_socket=mobo_socket &&
				ram_type=mobo_ram_type &&
				psu_power>=GREATEST(cpu_tdp,gpu_system_power);
	IF RESULT THEN
		SET output='COMPABILITY SUCCESS';
	ELSE
		SET output='COMPABILITY FAILURE';
	END IF;
END$
DELIMITER ;