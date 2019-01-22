/*
Navicat SQL Server Data Transfer

Source Server         : 公司SqlServer
Source Server Version : 105000
Source Host           : 192.168.10.12:1433
Source Database       : CPTIQA
Source Schema         : dbo

Target Server Type    : SQL Server
Target Server Version : 105000
File Encoding         : 65001

Date: 2018-12-26 14:12:29
*/


-- ----------------------------
-- Table structure for Logs
-- ----------------------------
DROP TABLE [dbo].[Logs]
GO
CREATE TABLE [dbo].[Logs] (
[ID] int NOT NULL IDENTITY(1,1) ,
[L_CLASSMETHOD] varchar(255) NULL ,
[L_CLASSNAME] varchar(255) NULL ,
[L_CONTENT] varchar(255) NULL ,
[L_CREATEDATE] varchar(255) NULL 
)


GO
DBCC CHECKIDENT(N'[dbo].[Logs]', RESEED, 74)
GO

-- ----------------------------
-- Indexes structure for table Logs
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table Logs
-- ----------------------------
ALTER TABLE [dbo].[Logs] ADD PRIMARY KEY ([ID])
GO
