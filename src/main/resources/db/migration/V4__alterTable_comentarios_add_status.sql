alter table comentarios add state tinyint;
update comentarios set state=1;