/*
 * This file contains software that has been made available under The BSD
 * license. Use and distribution hereof are subject to the restrictions set
 * forth therein.
 * 
 * Copyright (c) 2003 TADA AB - Taby Sweden
 * All Rights Reserved
 */
package org.postgresql.pljava.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import org.postgresql.pljava.TriggerException;
import org.postgresql.pljava.TriggerData;
import org.postgresql.pljava.Session;

/**
 * This class contains some triggers that I found written in C under the
 * contrib/spi directory of the postgres source distribution. Code to create the
 * necessary tables, functions, triggers, and some code to actually
 * execute them can be found in class {@link org.postgresql.pljava.test.Tester}.
 *
 * @author Thomas Hallgren
 */
public class Triggers
{
	/**
	 * insert user name in response to a trigger.
	 */
	static void insertUsername(TriggerData td)
	throws SQLException
	{
		if(td.isFiredForStatement())
			throw new TriggerException(td, "can't process STATEMENT events");

		if(td.isFiredAfter())
			throw new TriggerException(td, "must be fired before event");

		if(td.isFiredByDelete())
			throw new TriggerException(td, "can't process DELETE events");

		ResultSet _new = td.getNew();
		String[] args = td.getArguments();
		if(args.length != 1)
			throw new TriggerException(td, "one argument was expected");

		if(_new.getString(args[0]) == null)
			_new.updateString(args[0], Session.current().getUserName());
	}

	static void afterUsernameUpdate(TriggerData td)
	throws SQLException
	{
		Logger log = Logger.getAnonymousLogger();
		if(td.isFiredForStatement())
			throw new TriggerException(td, "can't process STATEMENT events");

		if(td.isFiredBefore())
			throw new TriggerException(td, "must be fired after event");

		if(!td.isFiredByUpdate())
			throw new TriggerException(td, "can't process DELETE or INSERT events");

		ResultSet _new = td.getNew();
		String[] args = td.getArguments();
		if(args.length != 1)
			throw new TriggerException(td, "one argument was expected");
		String colName = args[0];

		ResultSet _old = td.getOld();
		log.info("Old name is \"" + _old.getString(colName) + '"');
		log.info("New name is \"" + _new.getString(colName) + '"');
	}

	/**
	 * Update a modification time when the row is updated.
	 */
	static void moddatetime(TriggerData td)
	throws SQLException
	{
		if(td.isFiredForStatement())
			throw new TriggerException(td, "can't process STATEMENT events");

		if(td.isFiredAfter())
			throw new TriggerException(td, "must be fired before event");

		if(!td.isFiredByUpdate())
			throw new TriggerException(td, "can only process UPDATE events");

		ResultSet _new = td.getNew();
		String[] args = td.getArguments();
		if(args.length != 1)
			throw new TriggerException(td, "one argument was expected");
		_new.updateTimestamp(args[0], new Timestamp(System.currentTimeMillis()));
	}
}