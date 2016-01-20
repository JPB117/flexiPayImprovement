UPDATE 
navmember JOIN
(SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2011 from cpd WHERE YEAR(startdate) = 2011 group by memberRegistrationNo) AS cpd_2011 ON cpd_2011.memberRegistrationNo = navmember.No_ 
JOIN (SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2012 from cpd WHERE YEAR(startdate) = 2012 group by memberRegistrationNo) AS cpd_2012 ON cpd_2012.memberRegistrationNo = navmember.No_ 
JOIN (SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2013 from cpd WHERE YEAR(startdate) = 2013 group by memberRegistrationNo) AS cpd_2013 ON cpd_2013.memberRegistrationNo = navmember.No_ 
JOIN (SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2014 from cpd WHERE YEAR(startdate) = 2014 group by memberRegistrationNo) AS cpd_2014 ON cpd_2014.memberRegistrationNo = navmember.No_ 
JOIN (SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2015 from cpd WHERE YEAR(startdate) = 2015 group by memberRegistrationNo) AS cpd_2015 on cpd_2015.memberRegistrationNo = navmember.No_
JOIN (SELECT memberRegistrationNo, SUM(cpdHours) AS YEAR_2016 from cpd WHERE YEAR(startdate) = 2016 group by memberRegistrationNo) AS cpd_2016 on cpd_2016.memberRegistrationNo = navmember.No_
set Year2011=cpd_2011.Year_2011,Year2012=cpd_2012.Year_2012,Year2013=cpd_2013.YEAR_2013,Year2014=cpd_2014.YEAR_2014,Year2015=cpd_2015.YEAR_2015,Year2016=cpd_2016.Year_2016
