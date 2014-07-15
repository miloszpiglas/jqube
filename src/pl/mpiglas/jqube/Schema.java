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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database schema - set of views (tables and queries).
 *
 */
public class Schema
{

    private Map<View, List<ViewsRelation>> viewRelations = new HashMap<View, List<ViewsRelation>>();
    
    /**
     * Adds new view to schema.
     * @param aView view
     */
    public void addView(View aView)
    {
        viewRelations.put(aView, new ArrayList<ViewsRelation>());
    }
    
    /**
     * Adds new view to schema and defines its relation to other view already defined in schema.
     * @param aView view
     * @param aRelation relation to other view
     */
    public void addView(View aView, ViewsRelation aRelation)
    {
        View related = aRelation.getRelatedView(aView);
        if (viewRelations.containsKey(related))
        {
            viewRelations.get(related).add(aRelation);
            viewRelations.put(aView, new ArrayList<>(Arrays.asList(aRelation)));
        }
        else
        {
            throw new IllegalArgumentException("Relation does not match view in schema");
        }
    }
    
    /**
     * @param aView view
     * @return all views related to given one.
     */
    public List<View> getRelatedViews(View aView)
    {
        if (viewRelations.containsKey(aView))
        {
            List<View> related = new ArrayList<>();
            for (ViewsRelation r : viewRelations.get(aView))
            {
                related.add(r.getRelatedView(aView));
            }
            return related;
        }
        throw new IllegalArgumentException("View not in schema");
    }
    
    /**
     * 
     * @param aName database name of view
     * @return view with given name, or null if view not found.
     */
    public View getView(String aName)
    {
        for (View v : viewRelations.keySet())
        {
            if (v.getName().equals(aName))
            {
                return v;
            }
        }
        return null;
    }
    
    /**
     * Tries to find in schema relation between two views.
     * @param aView view
     * @param aOther view
     * @return relation.
     * @throws IllegalArgumentException if relation for given views does not exist.
     */
    public ViewsRelation getRelationOfViews(View aView, View aOther)
    {
        if (viewRelations.containsKey(aView))
        {
            for (ViewsRelation r : viewRelations.get(aView))
            {
                if (r.isViewRelation(aOther))
                {
                    return r;
                }
            }
        }
        throw new IllegalArgumentException("Views are not related");
    }
    
}
