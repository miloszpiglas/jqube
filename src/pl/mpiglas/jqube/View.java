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

/**
 * Represents table in database or query.
 * 
 */
public interface View
{
    /**
     * 
     * @return user-friendly view's name.
     */
    String getName();

    /**
     * 
     * @return source of values - name of table or query.
     */
    String getSource();

    /**
     * 
     * @param aName attribute's database name.
     * @return attribute with given name for this view
     */
    ViewAttribute getAttribute(String aName);

    /**
     * 
     * @return names of all attributes in this view.
     */
    List<String> getAttributeNames();

    /**
     * 
     * @return all attributes of this view.
     */
    List<ViewAttribute> getAttributes();
}
