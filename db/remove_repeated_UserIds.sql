create temporary table tmpTable (id int);
insert  tmpTable
        (id);
select userId from member group by userId having count(userId)>1;

update member m set userId=(select id from user u where m.memberNo=u.memberNo) where m.userId IN(select id from tmpTable);
