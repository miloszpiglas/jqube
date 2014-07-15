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

import java.util.List;
import java.util.Map;

/**
 * Query - statement string and query parameters.
 */
public class Query
{
    private final String statement;
    private final Map<ViewAttribute, List<Integer>> params;
    private final List<ViewAttribute> selectAttributes;

    /**
     * Inits query with statement string and parameters
     * 
     * @param aStatement
     *            full statement string.
     * @param aParams
     *            map of attributes with assigned indexes of parameters.
     * @param aAttributes
     *            list of attributes used in query.
     */
    public Query(String aStatement, Map<ViewAttribute, List<Integer>> aParams,
            List<ViewAttribute> aAttributes)
    {
        super();
        this.statement = aStatement;
        this.params = aParams;
        this.selectAttributes = aAttributes;
    }

    public final String getStatement()
    {
        return statement;
    }

    public final Map<ViewAttribute, List<Integer>> getParams()
    {
        return params;
    }

    public final List<ViewAttribute> getSelectAttributes()
    {
        return selectAttributes;
    }

}
