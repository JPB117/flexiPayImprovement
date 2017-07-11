CREATE TABLE IF NOT EXISTS docnums (
  casenumber varchar(60) NOT NULL,
  doctype varchar(45) NOT NULL,
  docno varchar(20) NOT NULL,
  PRIMARY KEY (casenumber,doctype)
);

CREATE TABLE IF NOT EXISTS doctypeseq (
  doctype varchar(45) NOT NULL,
  startval int DEFAULT 0,
  nextval int NOT NULL,
  PRIMARY KEY (doctype)
);

drop function if exists `proc_generatedocnum`;

DELIMITER $$
CREATE FUNCTION `proc_generatedocnum`(p_doctype varchar(45), p_casenumber varchar(45)) returns varchar(100) DETERMINISTIC
BEGIN
	declare v_startval int;
	declare v_nextval int;	
        declare v_filler char(5);
	declare v_previousallocatedno varchar(20);
	
	set p_doctype=upper(p_doctype);
	set v_startval = (select startval from doctypeseq where doctype=p_doctype);
	set v_nextval= (select nextval  from doctypeseq where doctype=p_doctype);	
	
	select docno into v_previousallocatedno from docnums where casenumber=p_casenumber and doctype=p_doctype;
	
	if (v_previousallocatedno is null) then
		if(v_startval is null) then
			
			insert into doctypeseq values (p_doctype, 0,1);
			set v_startval = 0;
			set v_nextval = v_startval+1;
		else
			set v_nextval = v_nextval+1;
			update doctypeseq set nextval= v_nextval where doctype=p_doctype;
		end if;
		
		if(v_nextval<10) then set v_filler = '000';
                elseif (v_nextval<100 ) then set v_filler = '00';
                elseif (v_nextval<1000 ) then set v_filler = '0';
                else set v_filler = '';
		end if;

		insert into docnums values (p_casenumber,p_doctype, concat(p_doctype,v_filler,v_nextval));
		return concat(p_doctype,v_filler,v_nextval);
	else
		return v_previousallocatedno;
	end if;
	
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS generate_docnum;
CREATE TRIGGER generate_docnum
BEFORE INSERT ON Invoice
FOR EACH ROW
SET NEW.documentNo= proc_generatedocnum('INV', concat('INV',NEW.refId));

DROP TRIGGER IF EXISTS generate_booking_ern_no;
CREATE TRIGGER generate_booking_ern_no
BEFORE INSERT ON delegate
FOR EACH ROW
SET NEW.ern= proc_generateErns('ERN', NEW.booking_id,concat('BookingDelegate-',NEW.refId));


DROP TRIGGER IF EXISTS generate_goodstandingcertrefno;
CREATE TRIGGER generate_goodstandingcertrefno
BEFORE INSERT ON GoodStandingCertificate
FOR EACH ROW
SET NEW.documentNo= proc_generatedocnum('GOODSTANDINGCERT', concat('ICPAK-GOODSTANDING-',NEW.refId));



/*Booking Search Indexes*/
create index idxEventRefId on event(refId);
create index idxDelegateMemberReg on delegate(memberRegistrationNo);
create index idxDelegateEmail on delegate(email);
create index idxDelegateFullName on delegate(fullName);
create index idxbookingEmail on booking(`E-mail` );
create index idxbookingCompany on booking(`company` );
create index idxBookingContact on booking(`Contact` );
create index idxInvoicedocNo on invoice(`documentNo` );
create index idxBookingRefId on invoice(`bookingRefId` );
create index idxBooking_RefId on booking(`refId` );
create index idxMemberRefId on member(`refId` );
create index idxMemberNo on member(`memberNo` );


drop function if exists  proc_generateErns;
DELIMITER $$
CREATE FUNCTION `proc_generateErns`(p_doctype varchar(45),p_bookingId int,p_rowIdentifier varchar(45)) returns varchar(100) DETERMINISTIC
BEGIN
	declare v_startval int;
	declare v_nextval int;	
    declare v_filler char(5);
	declare v_previousallocatedno varchar(20);
	declare v_event_id bigint(20);	
	declare v_event_date datetime;
	declare v_deployment_date datetime;
	
	set v_deployment_date=cast('2016-09-06' as date);
	select event_id into v_event_id from booking where id=p_bookingId;
	select created into v_event_date from event where id=v_event_id;
	
	if(v_event_date>v_deployment_date) then
		#introduce new ERN generation fomula - i.e New set of ERNS per event (0-n)
		select concat(p_doctype,'-',v_event_id) into p_doctype from booking where id=p_bookingId;
	end if;	
 
	set p_doctype=upper(p_doctype);
	set v_startval = (select startval from doctypeseq where doctype=p_doctype);
	set v_nextval= (select nextval  from doctypeseq where doctype=p_doctype);	
	
	select docno into v_previousallocatedno from docnums where casenumber=p_rowIdentifier and doctype=p_doctype;
	
	if (v_previousallocatedno is null) then
		if(v_startval is null) then
			
			insert into doctypeseq values (p_doctype, 0,1);
			set v_startval = 0;
			set v_nextval = v_startval+1;
		else
			set v_nextval = v_nextval+1;
			update doctypeseq set nextval= v_nextval where doctype=p_doctype;
		end if;
		
		if(v_nextval<10) then set v_filler = '000';
                elseif (v_nextval<100 ) then set v_filler = '00';
                elseif (v_nextval<1000 ) then set v_filler = '0';
                else set v_filler = '';
		end if;

		insert into docnums values (p_rowIdentifier,p_doctype, concat(p_doctype,v_filler,v_nextval));
		return concat(p_doctype,'-',v_filler,v_nextval);
	else
		return v_previousallocatedno;
	end if;
	
END$$
DELIMITER ;
