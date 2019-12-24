CREATE TABLE User	(
userName char (20),
nameOfUser char (20),
email char (20),
userID char (9) not null primary key,
alarmID int,
groupID int);

CREATE TABLE UserAlarms	(
alarmID int primary key,
timeOfDay dateTime,
frequency date,
soundOption int,
vibration bit not null,
vibrationOptions int,
snooze bit not null,
GPS bit not null,
GPSCoords float,
GPSRange float);


