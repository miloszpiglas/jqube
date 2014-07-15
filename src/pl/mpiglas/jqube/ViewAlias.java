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
 * Alias of view used in query.
 * 
 */
public class ViewAlias
{
    private final View view;

    private final String alias;

    /**
     * Inits alias for given view.
     * @param aView view
     * @param aAlias alias string
     */
    public ViewAlias(View aView, String aAlias)
    {
        super();
        this.view = aView;
        this.alias = aAlias;
    }

    public final View getView()
    {
        return view;
    }

    public final String getAlias()
    {
        return alias;
    }
    
    

}
