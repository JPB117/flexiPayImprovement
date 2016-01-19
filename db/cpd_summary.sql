alter table navmember change column Year2011  Year2011 double default 0.0;
alter table navmember change column Year2012  Year2012 double default 0.0;
alter table navmember change column Year2013  Year2013 double default 0.0;
alter table navmember change column Year2014  Year2014 double default 0.0;
alter table navmember change column Year2015  Year2015 double default 0.0;
alter table navmember change column Year2016  Year2016 double default 0.0;
/*alter table navmember change column Year2017  Year2017 double default 0.0;*/

drop procedure if exists `proc_updatecpdsummary`;

DELIMITER $$
CREATE procedure `proc_updatecpdsummary`(p_memberRegistrationNo varchar(45), p_created date)
BEGIN
	declare v_year int;
	declare v_sum double(10,8);
	declare v_email varchar(50);
	declare v_status varchar(50);
	declare v_customerType varchar(50);
	declare v_name varchar(50);
	declare v_practisingNo varchar(50); 
	declare v_gender varchar(50);
	

	set v_year=YEAR(p_created); 
	
	select sum(cpdHours) into v_sum from cpd where memberRegistrationNo=p_memberRegistrationNo and YEAR(startdate)=v_year and status='Approved';

	if(select exists(select * from navmember where No_=p_memberRegistrationNo)=false) then
	
		select u.Name,u.email,m.customerType,m.practisingNo,u.Gender
		into v_name, v_email, v_customerType,v_practisingNo,v_gender
		from member m 
		inner join `user` u on (m.userId= u.id) where m.memberNo=p_memberRegistrationNo;
		
		insert into navmember(No_,Name,`E-Mail`,`Customer Type`, `Practising No`,Gender) 
			values(p_memberRegistrationNo, v_email,v_customerType,v_practisingNo, v_gender);
	end if;
	
	if(v_year=2011)	then
		update navmember set Year2011=v_sum where No_=p_memberRegistrationNo;
	elseif(v_year=2012) then
		update navmember set Year2012=v_sum where No_=p_memberRegistrationNo;
	elseif(v_year=2013) then
		update navmember set Year2013=v_sum where No_=p_memberRegistrationNo;
	elseif(v_year=2014) then
		update navmember set Year2014=v_sum where No_=p_memberRegistrationNo;
	elseif(v_year=2015) then
		update navmember set Year2015=v_sum where No_=p_memberRegistrationNo;
	elseif(v_year=2016) then
		update navmember set Year2016=v_sum where No_=p_memberRegistrationNo;
	elseif(v_year=2017) then
		update navmember set Year2017=v_sum where No_=p_memberRegistrationNo;
	end if;
	
END$$
DELIMITER ;


drop procedure if exists `proc_updatecpdFromCPDHours`;
DELIMITER $$

CREATE procedure `proc_updatecpdFromCPDHours`(p_memberRegistrationNo varchar(45), p_created date)
BEGIN
UPDATE 
navmember JOIN
(SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2011 from cpd WHERE YEAR(startdate) = 2011 and memberRegistrationNo=p_memberRegistrationNo and status='Approved' group by memberRegistrationNo) AS cpd_2011 ON cpd_2011.memberRegistrationNo = navmember.No_ 
JOIN (SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2012 from cpd WHERE YEAR(startdate) = 2012 and memberRegistrationNo=p_memberRegistrationNo and status='Approved' group by memberRegistrationNo) AS cpd_2012 ON cpd_2012.memberRegistrationNo = navmember.No_ 
JOIN (SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2013 from cpd WHERE YEAR(startdate) = 2013 and memberRegistrationNo=p_memberRegistrationNo and status='Approved'  group by memberRegistrationNo) AS cpd_2013 ON cpd_2013.memberRegistrationNo = navmember.No_ 
JOIN (SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2014 from cpd WHERE YEAR(startdate) = 2014 and memberRegistrationNo=p_memberRegistrationNo and status='Approved'  group by memberRegistrationNo) AS cpd_2014 ON cpd_2014.memberRegistrationNo = navmember.No_ 
JOIN (SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2015 from cpd WHERE YEAR(startdate) = 2015 and memberRegistrationNo=p_memberRegistrationNo and status='Approved'  group by memberRegistrationNo) AS cpd_2015 on cpd_2015.memberRegistrationNo = navmember.No_
JOIN (SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2016 from cpd WHERE YEAR(startdate) = 2016 and memberRegistrationNo=p_memberRegistrationNo and status='Approved'  group by memberRegistrationNo) AS cpd_2016 on cpd_2016.memberRegistrationNo = navmember.No_
set Year2011=cpd_2011.Year_2011,Year2012=cpd_2012.Year_2012,Year2013=cpd_2013.YEAR_2013,Year2014=cpd_2014.YEAR_2014,Year2015=cpd_2015.YEAR_2015,Year2016=cpd_2016.Year_2016;
END$$
DELIMITER ;


DROP TRIGGER IF EXISTS updatecpdsummary;
DELIMITER $$
CREATE TRIGGER updatecpdsummary
AFTER UPDATE ON cpd
FOR EACH ROW
BEGIN
	if(NEW.status='Approved') then
	call proc_updatecpdFromCPDHours(NEW.memberRegistrationNo, NEW.startdate);
    end if;
END
$$
DELIMITER ;


DROP TRIGGER IF EXISTS insertcpdsummary;
DELIMITER $$
CREATE TRIGGER insertcpdsummary
AFTER INSERT ON cpd
FOR EACH ROW
BEGIN
	call proc_updatecpdsummary(NEW.memberRegistrationNo, NEW.startdate);
END
$$
DELIMITER ;
