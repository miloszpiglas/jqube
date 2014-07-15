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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Single table in database.
 *
 */
public class TableView implements View
{

    private String source;
    private String name;
    private Map<String, ViewAttribute> attributes;

    /**
     * Constructor.
     * @param aName user-friendly name of view. 
     * @param aSource database name.
     */
    public TableView(String aName, String aSource)
    {
        name = aName;
        source = aSource;
        attributes = new HashMap<String, ViewAttribute>();
    }
    
    /**
     * Adds attribute to view.
     * @param aDbName database name
     * @param aUserName user-friendly name (optional)
     * @param aSqlType sql datatype.
     */
    public void addAttribute(String aDbName, String aUserName, int aSqlType)
    {
        attributes.put(aDbName, new ViewAttribute(aDbName, this, aUserName, aSqlType));
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
     * @see pl.mpiglas.jqube.View#getSource()
     */
    @Override
    public String getSource()
    {
        return source;
    }

    /**
     * @see pl.mpiglas.jqube.View#getAttribute(java.lang.String)
     */
    @Override
    public ViewAttribute getAttribute(String aName)
    {
        return attributes.get(aName);
    }

    /**
     * @see pl.mpiglas.jqube.View#getAttributeNames()
     */
    @Override
    public List<String> getAttributeNames()
    {
        return new ArrayList<>(attributes.keySet());
    }

    /**
     * @see pl.mpiglas.jqube.View#getAttributes()
     */
    @Override
    public List<ViewAttribute> getAttributes()
    {
        return new ArrayList<>(attributes.values());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TableView other = (TableView) obj;
        if (attributes == null)
        {
            if (other.attributes != null)
                return false;
        } else if (!attributes.equals(other.attributes))
            return false;
        if (name == null)
        {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (source == null)
        {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        return true;
    }
    
    

}
