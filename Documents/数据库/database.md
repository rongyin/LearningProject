# procedure
```
Procedure name is
    cursor c_name is
        select * from table;
        
Begin
    for oneline in c_name loop
        **begin**
        update..
        commit;
        
        exception
            when others then
            rollback;
            commit;
         **end;**
       end loop;
END;
```
       
        
        
       