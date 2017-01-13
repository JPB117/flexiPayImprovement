insert into ApplicationCategory(refid,type,applicationamount,renewalamount,description) values 
					('0CigiyXk0zGlkOXw','NON_PRACTISING',26000,10000,'CPA graduates with less than 3 years experience in; Local law, Taxation, Accounting/auditing'),
					('kmkfda3412idfafk','ASSOCIATE',2500,2000,'CPA graduates or Holders of Foreign Accountancy with less than 3 years experience in Local law, Taxation, Accounting/auditing'),
					('ag4m4bvZnvgOpB0m','PRACTISING',10000,20000,'All existing ICPAK members who have experience of an additional 2 years or more in external audit supervised by a licensed practising member of ICPAK.'),
					('AssQn27RNrb1e2te','FOREIGN',26000,7750,'This admits members who are non-residents of Kenya.');

INSERT INTO `user` (`id`, `created`, `createdBy`, `isActive`, `refId`, `updated`, `updatedBy`, `address`, `city`, `email`, `memberId`, `nationality`, `password`, `phoneNumber`, `residence`, `ageGroup`, `Birth Cert`, `Blood Group`, `delegateId`, `Date Of Birth`, `firstName`, `Name`, `Gender`, `Height`, `ID No`, `isOverseas`, `lastName`, `Marital Status`, `Citizenship`, `Name 2`, `Payments By`, `Picture`, `Religion`, `Search Name`, `title`, `Student Type`, `Weight`, `username`, `status`) VALUES
(35, '2015-07-13 23:38:45', NULL, 1, 'omy4w7bRRKEUFP9Z', NULL, NULL, NULL, NULL, 'Administrator', NULL, NULL, 'e6c3da5b206634d7f3f3586d747ffdb36b5c675757b380c6a5fe5c570c714349', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Admin', NULL, NULL, '0.00000000000000000000', NULL, b'0', 'Admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00000000000000000000', 'Administrator', 'NEWACC');


INSERT INTO `role` (`id`, `created`, `createdBy`, `isActive`, `refId`, `updated`, `updatedBy`, `description`, `name`) VALUES
(1, '2015-07-13 22:32:27', NULL, 1, 'ngfLt4ERZm0hQtXp', NULL, NULL, 'Administrator', 'ADMIN'),
(2, '2015-07-13 22:32:44', NULL, 1, '2qU5tu87NF24jPeh', NULL, NULL, 'User', 'General User');


INSERT INTO `user_role` (`userid`, `roleid`) VALUES
(35, 1);
