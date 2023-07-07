IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'Shop')
CREATE DATABASE Shop
go

USE Shop
go

DROP TABLE if exists [TransactionShop]
go

DROP TABLE if exists [TransactionBuyer]
go

DROP TABLE if exists [Transaction]
go

DROP TABLE if exists [Item]
go

DROP TABLE if exists [Order]
go

DROP TABLE if exists [Buyer]
go

DROP TABLE if exists [Connection]
go

DROP TABLE if exists [Catalog]
go

DROP TABLE if exists [Shop]
go

DROP TABLE if exists [Member]
go

DROP TABLE if exists [City]
go

DROP TABLE if exists [Article]
go

CREATE TABLE [Article]
( 
	[ID]                 integer  IDENTITY  NOT NULL ,
	[Name]               varchar(100)  NOT NULL ,
	[Price]              decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [Buyer]
( 
	[ID]                 integer  NOT NULL ,
	[Name]               varchar(100)  NOT NULL ,
	[Credit]             decimal(10,3)  NOT NULL 
	CONSTRAINT [DefaultZero_468344172]
		 DEFAULT  0
)
go

CREATE TABLE [Catalog]
( 
	[Count]              integer  NOT NULL 
	CONSTRAINT [DefaultZero_717321581]
		 DEFAULT  0,
	[ArticleID]          integer  NOT NULL ,
	[ShopID]             integer  NOT NULL ,
	[ID]                 integer  IDENTITY  NOT NULL 
)
go

CREATE TABLE [City]
( 
	[ID]                 integer  IDENTITY  NOT NULL ,
	[Name]               varchar(100)  NOT NULL 
)
go

CREATE TABLE [Connection]
( 
	[ID]                 integer  IDENTITY  NOT NULL ,
	[Distance]           integer  NOT NULL ,
	[CityID1]            integer  NOT NULL ,
	[CityID2]            integer  NOT NULL 
)
go

CREATE TABLE [Item]
( 
	[ID]                 integer  IDENTITY  NOT NULL ,
	[ArticleID]          integer  NOT NULL ,
	[OrderID]            integer  NOT NULL ,
	[Count]              integer  NOT NULL ,
	[Price]              decimal(10,3)  NOT NULL 
	CONSTRAINT [DefaultZero_1157229798]
		 DEFAULT  0,
	[Discount]           integer  NOT NULL 
	CONSTRAINT [DefaultZero_18651025]
		 DEFAULT  0
)
go

CREATE TABLE [Member]
( 
	[ID]                 integer  IDENTITY  NOT NULL ,
	[CityID]             integer  NOT NULL 
)
go

CREATE TABLE [Order]
( 
	[ID]                 integer  IDENTITY  NOT NULL ,
	[BuyerID]            integer  NOT NULL ,
	[Status]             varchar(100)  NOT NULL 
	CONSTRAINT [DefaultCreated_2003130577]
		 DEFAULT  'created'
	CONSTRAINT [OrderStatus_318599559]
		CHECK  ( [Status]='created' OR [Status]='sent' OR [Status]='arrived' ),
	[Price]              decimal(10,3)  NOT NULL 
	CONSTRAINT [DefaultZero_470412434]
		 DEFAULT  0,
	[SentTime]           datetime  NULL ,
	[ReceivedTime]       datetime  NULL ,
	[Discount]           integer  NOT NULL 
	CONSTRAINT [DefaultZero_1430620049]
		 DEFAULT  0,
	[CityID]             integer  NULL ,
	[Assembled]          bit  NOT NULL 
	CONSTRAINT [DefaultZero_1096387030]
		 DEFAULT  0
)
go

CREATE TABLE [Shop]
( 
	[Discount]           integer  NOT NULL 
	CONSTRAINT [DefaultZero_69634971]
		 DEFAULT  0,
	[ID]                 integer  NOT NULL ,
	[Name]               varchar(100)  NOT NULL ,
	[Balance]            decimal(10,3)  NOT NULL 
	CONSTRAINT [DefaultZero_51296138]
		 DEFAULT  0
)
go

CREATE TABLE [Transaction]
( 
	[ID]                 integer  IDENTITY  NOT NULL ,
	[Amount]             decimal(10,3)  NOT NULL ,
	[ExecutionTime]      datetime  NULL ,
	[OrderID]            integer  NOT NULL 
)
go

CREATE TABLE [TransactionBuyer]
( 
	[ID]                 integer  NOT NULL 
)
go

CREATE TABLE [TransactionShop]
( 
	[ID]                 integer  NOT NULL ,
	[ShopID]             integer  NOT NULL 
)
go

ALTER TABLE [Article]
	ADD CONSTRAINT [XPKArticle] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [Buyer]
	ADD CONSTRAINT [XPKBuyer] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [Catalog]
	ADD CONSTRAINT [XPKCatalog] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XPKCity] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XAK1City] UNIQUE ([Name]  ASC)
go

ALTER TABLE [Connection]
	ADD CONSTRAINT [XPKConnection] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [Item]
	ADD CONSTRAINT [XPKItem] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [Member]
	ADD CONSTRAINT [XPKMember] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [Order]
	ADD CONSTRAINT [XPKOrder] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [Shop]
	ADD CONSTRAINT [XPKShop] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [Shop]
	ADD CONSTRAINT [XAK1Shop] UNIQUE ([Name]  ASC)
go

ALTER TABLE [Transaction]
	ADD CONSTRAINT [XPKTransaction] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [TransactionBuyer]
	ADD CONSTRAINT [XPKTransactionBuyer] PRIMARY KEY  CLUSTERED ([ID] ASC)
go

ALTER TABLE [TransactionShop]
	ADD CONSTRAINT [XPKTransactionShop] PRIMARY KEY  CLUSTERED ([ID] ASC)
go


ALTER TABLE [Buyer]
	ADD CONSTRAINT [R_18] FOREIGN KEY ([ID]) REFERENCES [Member]([ID])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Catalog]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([ArticleID]) REFERENCES [Article]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Catalog]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([ShopID]) REFERENCES [Shop]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Connection]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([CityID1]) REFERENCES [City]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Connection]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([CityID2]) REFERENCES [City]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Item]
	ADD CONSTRAINT [R_21] FOREIGN KEY ([ArticleID]) REFERENCES [Article]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Item]
	ADD CONSTRAINT [R_22] FOREIGN KEY ([OrderID]) REFERENCES [Order]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Member]
	ADD CONSTRAINT [R_19] FOREIGN KEY ([CityID]) REFERENCES [City]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Order]
	ADD CONSTRAINT [R_20] FOREIGN KEY ([BuyerID]) REFERENCES [Buyer]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Order]
	ADD CONSTRAINT [R_29] FOREIGN KEY ([CityID]) REFERENCES [City]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Shop]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([ID]) REFERENCES [Member]([ID])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Transaction]
	ADD CONSTRAINT [R_25] FOREIGN KEY ([OrderID]) REFERENCES [Order]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [TransactionBuyer]
	ADD CONSTRAINT [R_26] FOREIGN KEY ([ID]) REFERENCES [Transaction]([ID])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [TransactionShop]
	ADD CONSTRAINT [R_27] FOREIGN KEY ([ID]) REFERENCES [Transaction]([ID])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [TransactionShop]
	ADD CONSTRAINT [R_28] FOREIGN KEY ([ShopID]) REFERENCES [Shop]([ID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go