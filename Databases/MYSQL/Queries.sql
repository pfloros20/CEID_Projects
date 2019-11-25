-- 1
SELECT mobo_model,COUNT(cpu_socket)
FROM `MOTHERBOARD` LEFT JOIN `CPU`
ON mobo_socket=cpu_socket
GROUP BY mobo_model ORDER BY COUNT(cpu_socket);
-- 2
SELECT position,year,AVG(amount) AS AVG
FROM `ADMIN`INNER JOIN `GOT_PAID`
ON id=admin_id
INNER JOIN `SALARY`
ON salary_id=`SALARY`.id
WHERE month IN(1,2,3)
GROUP BY year,position;
-- 3
SELECT `SSD`.ssd_model AS MODEL,ssd_capacity AS CAPACITY,ssd_connection AS CONN_TYPE,ssd_price AS PRICE,COUNT(`ORDER`.ssd_model) AS TIMES_ORDERED
FROM `SSD` LEFT JOIN `ORDER` ON `ORDER`.ssd_model=`SSD`.ssd_model
WHERE order_date>=DATE_SUB(NOW(),INTERVAL 2 MONTH)
GROUP BY(`SSD`.ssd_model)
UNION ALL
SELECT `HDD`.hdd_model AS MODEL,hdd_capacity AS CAPACITY,hdd_connection AS CONN_TYPE,hdd_price AS PRICE,COUNT(`ORDER`.hdd_model) AS TIMES_ORDERED
FROM `HDD` LEFT JOIN `ORDER` ON `ORDER`.hdd_model=`HDD`.hdd_model
WHERE order_date>=DATE_SUB(NOW(),INTERVAL 2 MONTH)
GROUP BY(`HDD`.hdd_model)
UNION ALL
SELECT `extHD`.exthd_model AS MODEL,exthd_capacity AS CAPACITY,exthd_connection AS CONN_TYPE,exthd_price AS PRICE,COUNT(`ORDER`.exthd_model) AS TIMES_ORDERED
FROM `extHD` LEFT JOIN `ORDER` ON `ORDER`.exthd_model=`extHD`.exthd_model
WHERE order_date>=DATE_SUB(NOW(),INTERVAL 2 MONTH)
GROUP BY(`extHD`.exthd_model);
-- 4
SELECT PSU_WITH_WATTAGE FROM
(SELECT CONCAT(`ORDER`.psu_model,' with ',`PSU`.psu_power) AS PSU_WITH_WATTAGE,COUNT(`ORDER`.psu_model) AS COUNT
FROM `ORDER` INNER JOIN `PSU` ON `ORDER`.psu_model=`PSU`.psu_model
GROUP BY(`ORDER`.psu_model)
HAVING `ORDER`.psu_model NOT LIKE '%Plus%'
ORDER BY(`ORDER`.psu_model) DESC LIMIT 3)AS T;
-- 5
SELECT name,surname FROM
`CUSTOMER` INNER JOIN `ORDER` ON `CUSTOMER`.id=customer_id
GROUP BY `CUSTOMER`.id
HAVING COUNT(`CUSTOMER`.id) = (SELECT MAX(count) FROM
(SELECT COUNT(`CUSTOMER`.id) AS count
FROM `CUSTOMER` INNER JOIN `ORDER` ON `CUSTOMER`.id=customer_id
GROUP BY `CUSTOMER`.id)AS T);
-- 6
SELECT Senior,GROUP_CONCAT(Junior SEPARATOR ', ') AS Junior FROM
(SELECT CONCAT(`SEN`.name,' ',`SEN`.surname) AS Senior,CONCAT(`JUN`.name,' ',`JUN`.surname) AS Junior
FROM `ADMIN` AS SEN INNER JOIN `ADMIN` AS JUN ON `SEN`.id=`JUN`.supervised_by)
AS T GROUP BY(Senior);