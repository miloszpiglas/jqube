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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Builder using database schema to prepare query from specified attributes.
 * 
 */
public class QueryBuilder
{
    private Schema schema;
    private List<SelectAttribute> attributes;
    private Tree tree;

    /**
     * Inits builder for specified database {@link Schema}.
     * 
     * @param aSchema
     */
    public QueryBuilder(Schema aSchema)
    {
        schema = aSchema;
        attributes = new ArrayList<>();
        tree = new Tree(schema);
    }

    /**
     * Adds new attribute do query.
     * 
     * @param aAttribute
     *            query's attribute.
     * @param aOuterJoin
     *            string used to define relation between two views.
     */
    public void add(SelectAttribute aAttribute, boolean aOuterJoin)
    {
        tree.addJoin(aAttribute.getView(), aOuterJoin);
        attributes.add(aAttribute);
    }

    /**
     * Adds new attribute with default join string (JOIN).
     * 
     * @param aAttribute
     *            query's attribute.
     */
    public void add(SelectAttribute aAttribute)
    {
        add(aAttribute, false);
    }

    private boolean equalsSetsPair(Set<SelectAttribute> aSet,
            Set<SelectAttribute> aUnionSet, Set<SelectAttribute> aOther)
    {
        if (aSet.size() != aUnionSet.size() + aOther.size())
        {
            return false;
        } else
        {
            return aSet.containsAll(aUnionSet) && aSet.containsAll(aOther);
        }
    }

    private boolean setsDisjoint(Set<SelectAttribute> aSet,
            Set<SelectAttribute> aOther)
    {
        for (SelectAttribute a : aSet)
        {
            if (aOther.contains(a))
            {
                return false;
            }
        }
        return true;
    }

    private void validate()
    {
        Set<SelectAttribute> groupSet = new HashSet<>();
        Set<SelectAttribute> aggregateSet = new HashSet<>();
        Set<SelectAttribute> visibleSet = new HashSet<>();
        for (SelectAttribute a : attributes)
        {
            if (a.isVisible())
            {
                visibleSet.add(a);
                if (a.getAggregation() != null)
                {
                    aggregateSet.add(a);
                }
                if (a.isGroupBy())
                {
                    groupSet.add(a);
                }
            }
        }
        if (aggregateSet.isEmpty() && groupSet.isEmpty()
                && !visibleSet.isEmpty())
        {
            return;
        } else if (!aggregateSet.isEmpty() && groupSet.isEmpty()
                && visibleSet.equals(aggregateSet))
        {
            return;
        } else if (!aggregateSet.isEmpty() && !groupSet.isEmpty()
                && equalsSetsPair(visibleSet, groupSet, aggregateSet)
                && setsDisjoint(groupSet, aggregateSet))
        {
            return;
        }
        throw new IllegalArgumentException("Aggregation and group by");
    }

    /**
     * Prepares view representing query. Such view might be added to schema and
     * used in new queries.
     * 
     * @param aViewName
     *            view's name.
     * @return query view.
     */
    public QueryView createView(String aViewName)
    {
        validate();
        return new QueryView(aViewName, attributes, tree);
    }

    /**
     * Builds query.
     * 
     * @return query - statement and parameters.
     */
    public Query build()
    {
        return createView("Query").getQuery();
    }
}
