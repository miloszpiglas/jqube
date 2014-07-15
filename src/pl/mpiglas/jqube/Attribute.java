/*
 * Select query builder
 * Copyright (C) 2014  Milosz Piglas [milosz@archeocs.com]
 *    
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.mpiglas.jqube;

import java.sql.Types;

/**
 * 
 * Attribute of relation.
 * 
 */
public interface Attribute
{

    /**
     * 
     * @return table or query this attribute is assigned to.
     */
    View getView();

    /**
     * 
     * @return name defined in view (query or table).
     */
    String getDbName();

    /**
     * 
     * @return view.name + '.' + dbName.
     */
    String getFullName();

    /**
     * 
     * @return sql datatype, one of values from {@link Types}.
     */
    int getSqlType();

    /**
     * 
     * @return user-friendly name of attribute. Possibly empty.
     */
    String getUserName();
}
