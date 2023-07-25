--CREATE DATABASE fpl_daotao
--GO
--USE fpl_daotao
--Go

CREATE TABLE students(
	id INT IDENTITY(1,1) PRIMARY KEY,
	student_code VARCHAR(10),
	fullname NVARCHAR(50),
	email VARCHAR(50),
	phone VARCHAR(11),
	gender BIT,
	[address] NVARCHAR(MAX),
	avatar VARCHAR(MAX)
)

CREATE TABLE Grade (
	id INT IDENTITY(1,1) PRIMARY KEY,
	student_id INT,
	english INT,
	informatics INT,
	gymnastics INT,
	CONSTRAINT FK_StudentId FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
)

CREATE TABLE users (
	username VARCHAR(30) PRIMARY KEY,
	[password] VARCHAR(50),
	[role] INT
)
GO

INSERT INTO students VALUES ('PH19601', N'Nguyễn Thượng Văn', 'vannt@fpt.edu', '0973412744', 1, N'Tây Hồ', 'No Avatar')
INSERT INTO students VALUES	('PH19602', N'Nguyễn Thị Trang', 'trangnt@fpt.edu', '0844123487', 0, N'Đan Phượng', 'No Avatar')
INSERT INTO students VALUES	('PH19603', N'Hoàng Như Sơn', 'sontnt@fpt.edu', '0855412365', 1, N'Thanh Hóa', 'No Avatar')
INSERT INTO students VALUES	('PH19604', N'Lê Thị Hạnh', 'hanhlh@fpt.edu', '0734554621', 0, N'Thanh Hóa', 'No Avatar')


INSERT INTO grade VALUES
	(1, 5, 7, 9),
	(2, 9, 8, 7),
	(3, 9, 8, 9),
	(4, 5, 3, 5)
	go
INSERT INTO users VALUES ('daotao1', 'Daotao1', 0), ('giangvien1', 'Giangvien1', 1);
go
DELETE FROM Students WHERE student_code LIKE 'PH224';
SELECT * FROM USERS;
SELECT * FROM Students;
SELECT * FROM Grade;

DELETE FROM Grade

SELECT G.id, S.student_code, S.fullname, G.english, G.informatics, G.gymnastics, ROUND(CAST((G.english + G.informatics + G.gymnastics) AS FLOAT) / 3, 1) AS average FROM Grade AS G JOIN students AS S ON G.student_id = S.id
SELECT TOP(3) G.id, S.student_code, S.fullname, G.english, G.informatics, G.gymnastics, ROUND(CAST((G.english + G.informatics + G.gymnastics) AS FLOAT) / 3, 1) AS average FROM Grade AS G JOIN students AS S ON G.student_id = S.id ORDER BY average DESC
SELECT G.id, S.student_code, S.fullname, G.english, G.informatics, G.gymnastics, ROUND(CAST((G.english + G.informatics + G.gymnastics) AS FLOAT) / 3, 1) AS average FROM Grade AS G JOIN students AS S ON G.student_id = S.id WHERE student_id = 2

