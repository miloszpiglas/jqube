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
 * Generates aliases for views.
 *
 */
public enum AliasGenerator
{
    INSTANCE;
    
    private char[] letters = "ABCDEFGHIKLMNOPQRSTUVXYZ".toCharArray();
    private int start = 1;
    private int len = letters.length;
    
    /**
     * 
     * @return next alias.
     */
    public String next()
    {
        int tmp = start;
        StringBuilder base = new StringBuilder();
        while (tmp > 0)
        {
            int idx = tmp % len;
            base.insert(0, letters[idx - 1]);
            tmp = tmp / len;
        }
        start++;
        return base.toString();
    }
}
