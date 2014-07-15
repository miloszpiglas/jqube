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
 * Pair of attributes - part of definition two tables' relation.
 */
public final class AttributesPair
{
    private final Attribute first;
    private final Attribute second;

    /**
     * Creates unordered pair of attributes.
     * @param aFirst view's attribute
     * @param aSecond view's attribute
     */
    public AttributesPair(Attribute aFirst, Attribute aSecond)
    {
        super();
        this.first = aFirst;
        this.second = aSecond;
    }

    public final Attribute getFirst()
    {
        return first;
    }

    public final Attribute getSecond()
    {
        return second;
    }

    private StringBuilder prepareJoinName(ViewAlias aAlias)
    {
        return new StringBuilder().append(aAlias.getAlias()).append(".")
                .append(attributeFromView(aAlias.getView()).getDbName());

    }

    /**
     * Finds and returns in pair attribute from view that is related to given one.
     * @param aOther a view
     * @return attribute that defines relation between its view and given one.
     */
    public Attribute getRelatedAttribute(View aOther)
    {
        if (aOther.equals(first.getView()))
        {
            return second;
        } else if (aOther.equals(second.getView()))
        {
            return first;
        } else
        {
            throw new IllegalArgumentException("View " + aOther.getName()
                    + " does not match pair");
        }
    }
    
    private Attribute attributeFromView(View aView)
    {
        if (aView.equals(first.getView()))
        {
            return first;
        } else if (aView.equals(second.getView()))
        {
            return second;
        } else
        {
            throw new IllegalArgumentException("View " + aView.getName()
                    + " does not match pair");
        }
    }

    /**
     * 
     * @param aView view
     * @return true if pair contains attribute from given view.
     */
    public boolean containsViewAttribute(View aView)
    {
        return aView.equals(first.getView()) || aView.equals(second.getView());
    }

    /**
     * Prepares string representing relation between two views.
     * @param aAlias alias name of view
     * @param aAlias2 alias name of view
     * @return prepared string: aAlias.attribute = aAlias2.attribute
     */
    public StringBuilder prepareString(ViewAlias aAlias, ViewAlias aAlias2)
    {
        if (aAlias == null || aAlias2 == null)
        {
            throw new IllegalArgumentException("Null alias");
        }
        StringBuilder builder = new StringBuilder();
        builder.append(prepareJoinName(aAlias)).append(" = ")
                .append(prepareJoinName(aAlias2));
        return builder;

    }

}
