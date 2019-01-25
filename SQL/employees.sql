CREATE TABLE employees (
  `email` varchar(50) primary key,
  `password` varchar(20) not null,
  `fullname` varchar(100)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='' CHECKSUM=0 DELAY_KEY_WRITE=0;