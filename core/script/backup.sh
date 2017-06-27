#!/bin/bash

cd $1
pg_dump -i -h localhost -p 5432 -U postgres -F p -b -v -f $2 vidrioluzdb
