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
 * View's attribute. If view represents relation (e.g. table), instance of this
 * class is an attribute of relation (column of table). If view represents
 * query, instance of this class is an selected attribute.
 * 
 * @author Milosz Piglas
 * 
 */
public class ViewAttribute implements Attribute
{

    /**
     * Aggregation functions enum.
     *
     */
    public static enum Aggregation
    {
        COUNT("COUNT");

        private String name;

        Aggregation(String aName)
        {
            name = aName;
        }

        public String getName()
        {
            return name;
        }
    }

    /**
     * 
     * Builds {@link SelectAttribute} - definition of attribute used to prepare query.
     *
     */
    public class SelectAttributeBuilder
    {
        private boolean orderBy = false;
        private boolean groupBy = false;
        private Condition condition;
        private boolean visible = true;
        private Aggregation aggregation;
        private String aliasName;

        /**
         * Constructor.
         * @param aDbName name of attribute in database
         * @param aView view this attribute is related to (table or query)
         * @param aUserName 
         * @param aSqlType type of attribute (one of values from {@link Types}.
         */
        public SelectAttributeBuilder(String aDbName, View aView,
                String aUserName, int aSqlType)
        {
            dbName = aDbName;
            view = aView;
            userName = aUserName;
            sqlType = aSqlType;
        }

        /**
         * @param aOrderBy true if attribute should be used in ORDER BY clause.
         * @return this
         */
        public SelectAttributeBuilder withOrderBy(boolean aOrderBy)
        {
            orderBy = aOrderBy;
            return this;
        }

        /**
         * 
         * @param aGroupBy true if attribute should be used in GROUP BY clause.
         * @return this
         */
        public SelectAttributeBuilder withGroupBy(boolean aGroupBy)
        {
            groupBy = aGroupBy;
            return this;
        }

        /**
         * 
         * @param aCondition condition in WHERE clause for this attribute.
         * @return this
         */
        public SelectAttributeBuilder withCondition(Condition aCondition)
        {
            condition = aCondition;
            return this;
        }

        /**
         * 
         * @param aVisible true if values of this attribute should be read by query.
         * @return this
         */
        public SelectAttributeBuilder withVisible(boolean aVisible)
        {
            visible = aVisible;
            return this;
        }

        /**
         * 
         * @param aFunction aggregation function that should be applied to this attribute.
         * @return this
         */
        public SelectAttributeBuilder withAggregate(Aggregation aFunction)
        {
            aggregation = aFunction;
            return this;
        }

        /**
         * 
         * @param aAlias alternative name for this attribute.
         * @return this
         */
        public SelectAttributeBuilder withAliasName(String aAlias)
        {
            aliasName = aAlias;
            return this;
        }

        /**
         * Uses state of builder to prepare {@link SelectAttribute}.
         * @return new attribute
         */
        public SelectAttribute build()
        {
            return new SelectAttribute(dbName, view, sqlType, visible, orderBy,
                    groupBy, condition, aggregation, aliasName);
        }
    }

    private String userName;
    private View view;
    private String dbName;
    private int sqlType;

    /**
     * Constructor.
     * @param aDbName name of attribute in database.
     * @param aView view this attribute is related to.
     * @param aUserName
     * @param aSqlType type of attribute (one of values from {@link Types}.
     */
    public ViewAttribute(String aDbName, View aView, String aUserName,
            int aSqlType)
    {
        dbName = aDbName;
        view = aView;
        userName = aUserName;
        sqlType = aSqlType;
    }

    @Override
    public View getView()
    {
        return view;
    }

    @Override
    public String getDbName()
    {
        return dbName;
    }

    @Override
    public String getFullName()
    {
        return view.getName() + "." + dbName;
    }

    @Override
    public int getSqlType()
    {
        return sqlType;
    }

    @Override
    public String getUserName()
    {
        return userName;
    }

    public SelectAttributeBuilder select()
    {
        return new SelectAttributeBuilder(dbName, view, userName, sqlType);
    }

    @Override
    public String toString()
    {
        return "ViewAttribute [userName=" + userName + ", view="
                + view.getName() + ", dbName=" + dbName + "]";
    }

}
