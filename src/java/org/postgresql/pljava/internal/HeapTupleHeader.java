/*
 * Copyright (c) 2003, 2004 TADA AB - Taby Sweden
 * Distributed under the terms shown in the file COPYRIGHT.
 */
package org.postgresql.pljava.internal;

import java.sql.SQLException;

/**
 * The <code>HeapTupleHeader</code> correspons to the internal PostgreSQL
 * <code>HeapTupleHeader</code> struct.
 *
 * @author Thomas Hallgren
 */
public class HeapTupleHeader extends NativeStruct
{
	/**
	 * Obtains a value from the underlying native <code>HeapTupleHeader</code>
	 * structure.
	 * @param index Index of value in the structure (one based).
	 * @return The value or <code>null</code>.
	 * @throws SQLException If the underlying native structure has gone stale.
	 */
	public Object getObject(int index)
	throws SQLException
	{
		synchronized(Backend.THREADLOCK)
		{
			return this._getObject(index);
		}
	}

	/**
	 * Obtains the TupleDesc from the underlying native <code>HeapTupleHeader
	 * </code> structure.
	 * @return The TupleDesc that describes this tuple.
	 * @throws SQLException If the underlying native structure has gone stale.
	 */
	public TupleDesc getTupleDesc()
	throws SQLException
	{
		synchronized(Backend.THREADLOCK)
		{
			return this._getTupleDesc();
		}
	}

	private native TupleDesc _getTupleDesc()
	throws SQLException;

	private native Object _getObject(int index)
	throws SQLException;
}
