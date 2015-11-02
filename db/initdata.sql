insert into ApplicationCategory(refid,type,applicationamount,renewalamount,description) values 
					('0CigiyXk0zGlkOXw',0,26000,10000,'All CPA graduates who have experience of 3 years or more'),
					('kmkfda3412idfafk',4,26000,7750,'CPA graduates or Holders of Foreign Accountancy with less than 3 years experience in Local law, Taxation, Accounting/auditing'),
					('ag4m4bvZnvgOpB0m',1,10000,20000,'All existing ICPAK members who have experience of an additional 2 years or more in external audit supervised by a licensed practising member of ICPAK.'),
					('AssQn27RNrb1e2te',2,26000,7750,'This admits members who are non-residents of Kenya.'), 
					('bo4seWbmA7xWi8FJ',3,2500,2000,'CPA graduates with less than 3 years experience in; Local law, Taxation, Accounting/auditing');

INSERT INTO `user` (`id`, `created`, `createdBy`, `isActive`, `refId`, `updated`, `updatedBy`, `address`, `city`, `email`, `memberId`, `nationality`, `password`, `phoneNumber`, `residence`, `ageGroup`, `Birth Cert`, `Blood Group`, `delegateId`, `Date Of Birth`, `firstName`, `Name`, `Gender`, `Height`, `ID No`, `isOverseas`, `lastName`, `Marital Status`, `Citizenship`, `Name 2`, `Payments By`, `Picture`, `Religion`, `Search Name`, `title`, `Student Type`, `Weight`, `username`, `status`) VALUES
(33, '2015-07-12 12:10:45', NULL, 1, 'gNtLJ03iEfS3LCac', '2015-07-12 12:11:56', NULL, 'P.o Box 34556', 'Nrb', 'kimani@wira.io', NULL, NULL, 'e6c3da5b206634d7f3f3586d747ffdb36b5c675757b380c6a5fe5c570c714349', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Kimani', NULL, NULL, '0.00000000000000000000', NULL, b'0', 'Muritu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00000000000000000000', 'kimani@wira.io', 'NEWACC'),
(34, '2015-07-12 13:38:11', NULL, 1, 'OYfPsqwsz4g8KPTB', '2015-07-12 13:38:34', NULL, '34300', 'Nrb', 'kimani@at.co.ke', NULL, NULL, '1ba3d16e9881959f8c9a9762854f72c6e6321cdd44358a10a4e939033117eab9', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Shikwekwe', NULL, NULL, '0.00000000000000000000', NULL, b'0', 'Martin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00000000000000000000', 'kimani@at.co.ke', 'NEWACC'),
(35, '2015-07-13 23:38:45', NULL, 1, 'omy4w7bRRKEUFP9Z', NULL, NULL, NULL, NULL, 'mdkimani@gmail.com', NULL, NULL, 'e6c3da5b206634d7f3f3586d747ffdb36b5c675757b380c6a5fe5c570c714349', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Admin', NULL, NULL, '0.00000000000000000000', NULL, b'0', 'Admin', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0.00000000000000000000', 'mdkimani@gmail.com', 'NEWACC');


INSERT INTO `role` (`id`, `created`, `createdBy`, `isActive`, `refId`, `updated`, `updatedBy`, `description`, `name`) VALUES
(1, '2015-07-13 22:32:27', NULL, 1, 'ngfLt4ERZm0hQtXp', NULL, NULL, 'Administrator', 'ADMIN'),
(2, '2015-07-13 22:32:44', NULL, 1, '2qU5tu87NF24jPeh', NULL, NULL, 'User', 'General User');


INSERT INTO `user_role` (`userid`, `roleid`) VALUES
(34, 1),
(35, 1),
(33, 2),
(34, 2);
