package com.icpak.rest.hibernate_ext;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.unique.DefaultUniqueDelegate;
import org.hibernate.metamodel.relational.UniqueKey;

public class Mysql5UniqueDelegate extends DefaultUniqueDelegate{
	
	public Mysql5UniqueDelegate(Dialect dialect) {
		super(dialect);
	}
	
	@Override
	public String getAlterTableToDropUniqueKeyCommand(UniqueKey uniqueKey) {
		// Do this here, rather than allowing UniqueKey/Constraint to do it.
		// We need full, simplified control over whether or not it happens.
		final String tableName = uniqueKey.getTable().getQualifiedName( dialect );
		final String constraintName = dialect.quote( uniqueKey.getName() );

		return "alter table " + tableName + " drop index " + constraintName;
	}
}
