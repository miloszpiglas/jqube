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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of view representing query. Such view might be used, as nested query, to build other ones.
 * 
 */
public class QueryView implements View
{

    private Tree tree;
    private List<SelectAttribute> attributes;
    private String name;

    /**
     * Inits view representing query.
     * @param aName user-friendly name of view.
     * @param aAttributes list of attributes used in query.
     * @param aTree tree represents relations between views used in this query.
     */
    public QueryView(String aName, List<SelectAttribute> aAttributes, Tree aTree)
    {
        name = aName;
        attributes = aAttributes;
        tree = aTree;
    }

    private boolean addToBuilder(StringBuilder aOutBuilder,
            List<StringBuilder> values, String aSeparator)
    {
        if (values.isEmpty())
        {
            return false;
        }
        aOutBuilder.append(values.get(0));
        if (values.size() > 1)
        {
            for (int i = 1; i < values.size(); i++)
            {
                aOutBuilder.append(aSeparator).append(values.get(i));
            }
        }
        return true;
    }

    private String buildQuery(boolean aWithParams)
    {
        StringBuilder query = new StringBuilder("SELECT");
        List<StringBuilder> attrs = new ArrayList<>();
        List<StringBuilder> order = new ArrayList<>();
        List<StringBuilder> group = new ArrayList<>();
        List<StringBuilder> where = new ArrayList<>();
        int paramCount = 0;
        for (SelectAttribute a : attributes)
        {
            String alias = tree.getViewAlias(a.getView());
            StringBuilder qn = a.prepareQueryName(alias);
            StringBuilder orderName = a.prepareOrderByName(alias);
            if (a.isVisible())
            {
                attrs.add(qn);
            }
            if (a.isOrderBy())
            {
                order.add(orderName);
            }
            if (a.isGroupBy())
            {
                group.add(qn);
            }
            if (aWithParams && a.getCondition() != null)
            {
                String name = a.prepareName(alias).toString();
                where.add(a.getCondition().prepareString(name, paramCount));
                paramCount = a.getCondition().getIndex() + 1;
            }
        }
        addToBuilder(query.append(" "), attrs, ", ");
        query.append(" FROM\n").append(tree.prepareString());
        if (!where.isEmpty())
        {
            addToBuilder(query.append("\n WHERE "), where, " ");
        }
        if (!group.isEmpty())
        {
            addToBuilder(query.append("\n GROUP BY "), group, ", ");
        }
        if (!order.isEmpty())
        {
            addToBuilder(query.append("\n ORDER BY "), order, ", ");
        }
        return query.toString();
    }

    /**
     * @see pl.mpiglas.jqube.View#getName()
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * @return statement without WHERE clause.
     */
    @Override
    public String getSource()
    {
        return "(" + buildQuery(false) + ")";
    }

    /**
     * @see pl.mpiglas.jqube.View#getAttribute(java.lang.String)
     */
    @Override
    public ViewAttribute getAttribute(String aName)
    {
        for (SelectAttribute a : attributes)
        {
            if (a.isVisible())
            {
                String attrName = a.getUserName() != null ? a.getUserName() : a
                        .getDbName();
                int type = a.getAggregation() != null ? Types.INTEGER : a
                        .getSqlType();
                if (attrName.equals(aName))
                {
                    return new ViewAttribute(attrName, this, null, type);
                }
            }

        }
        throw new IllegalArgumentException("Attribute " + aName + " not found");
    }

    /**
     * @see pl.mpiglas.jqube.View#getAttributeNames()
     */
    @Override
    public List<String> getAttributeNames()
    {
        List<String> names = new ArrayList<>();
        for (SelectAttribute a : attributes)
        {
            if (a.isVisible())
            {
                String attrName = a.getUserName() != null ? a.getUserName() : a
                        .getDbName();
                names.add(attrName);
            }
        }
        return names;
    }

    /**
     * @see pl.mpiglas.jqube.View#getAttributes()
     */
    @Override
    public List<ViewAttribute> getAttributes()
    {
        List<ViewAttribute> attrs = new ArrayList<>();
        for (SelectAttribute a : attributes)
        {
            if (a.isVisible())
            {
                String attrName = a.getUserName() != null ? a.getUserName() : a
                        .getDbName();
                int type = a.getAggregation() != null ? Types.INTEGER : a
                        .getSqlType();
                attrs.add(new ViewAttribute(attrName, this, null, type));
            }
        }
        return attrs;
    }

    private String getQueryString()
    {
        return buildQuery(true);
    }

    private Map<ViewAttribute, List<Integer>> getQueryParams()
    {
        int paramsCount = 1;
        Map<ViewAttribute, List<Integer>> params = new HashMap<ViewAttribute, List<Integer>>();
        for (SelectAttribute a : attributes)
        {
            if (a.getCondition() != null)
            {
                String attrName = a.getUserName() != null ? a.getUserName() : a
                        .getDbName();
                int type = a.getAggregation() != null ? Types.INTEGER : a
                        .getSqlType();
                List<Integer> indexes = a.getCondition().getParamsIndexes(
                        paramsCount);
                params.put(new ViewAttribute(attrName, this, null, type),
                        indexes);
                paramsCount = indexes.get(indexes.size() - 1) + 1;
            }
        }
        return params;

    }

    /**
     * 
     * @return ready to run statement and its parameters build from this view.
     */
    public Query getQuery()
    {
        return new Query(getQueryString(), getQueryParams(), getAttributes());
    }

}
