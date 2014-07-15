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

/**
 * 
 * Attribute with additional properties used to build query.
 * 
 */
public class SelectAttribute extends ViewAttribute
{

    private boolean visible;
    private boolean orderBy;
    private boolean groupBy;
    private Condition condition;
    private Aggregation aggregation;

    /**
     * Inits query's attribute.
     * 
     * @param aDbName
     *            database name
     * @param aView
     *            view this attribute is assigne to.
     * @param aSqlType
     *            sql datatype of attribute
     * @param aVisible
     *            if true, value of this attriubte is read in query
     * @param aOrderBy
     *            if true this attribute is used in ORDER BY clause
     * @param aGroupBy
     *            if true this attribute is used in GROUP BY clause
     * @param aCondition
     *            if not null, this attribute is in WHERE clasue with specified
     *            conidition(s)
     * @param aAggregation
     *            if not null, aggregation function is applied to this
     *            attribute.
     * @param aAliasName
     *            alias name.
     */
    public SelectAttribute(String aDbName, View aView, int aSqlType,
            boolean aVisible, boolean aOrderBy, boolean aGroupBy,
            Condition aCondition, Aggregation aAggregation, String aAliasName)
    {
        super(aDbName, aView, aAliasName, aSqlType);
        visible = aVisible;
        orderBy = aOrderBy;
        groupBy = aGroupBy;
        condition = aCondition;
        aggregation = aAggregation;
    }

    public final boolean isVisible()
    {
        return visible;
    }

    public final boolean isOrderBy()
    {
        return orderBy;
    }

    public final boolean isGroupBy()
    {
        return groupBy;
    }

    public final Condition getCondition()
    {
        return condition;
    }

    public final Aggregation getAggregation()
    {
        return aggregation;
    }

    /**
     * 
     * @param aViewAlias
     *            alias of view this attribute is assigned to.
     * @return name used in SELECT clause and GROUP BY clause.
     */
    public StringBuilder prepareQueryName(String aViewAlias)
    {
        StringBuilder builder = prepareName(aViewAlias);
        if (aggregation != null)
        {
            builder.insert(0, "(").insert(0, aggregation.getName()).append(")");
        }
        if (getUserName() != null)
        {
            builder.append(" as ").append(getUserName());
        }
        return builder;
    }

    /**
     * 
     * @param aViewAlias
     *            alias of view this attribute is assigned to.
     * @return name of attribute used in ORDER BY clause.
     */
    public StringBuilder prepareOrderByName(String aViewAlias)
    {
        if (getUserName() != null)
        {
            return new StringBuilder(getUserName());
        } else
        {
            return prepareName(aViewAlias);
        }
    }

    /**
     * 
     * @param aViewAlias
     *            alias of view this attribute is assigned to.
     * @return name of attribute.
     */
    public StringBuilder prepareName(String aViewAlias)
    {
        return new StringBuilder().append(aViewAlias).append(".")
                .append(getDbName());
    }

}
