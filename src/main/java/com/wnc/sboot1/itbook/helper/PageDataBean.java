
package com.wnc.sboot1.itbook.helper;

import java.util.ArrayList;
import java.util.List;

import com.wnc.basic.BasicNumberUtil;

public class PageDataBean<E> extends ArrayList<E>
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long pageNum;
    private long pages;
    private long pageSize;
    private long total;

    public PageDataBean( List<E> list,long page,long size,long totalRows )
    {
        this.addAll( list );
        this.setPageNum( page );
        this.setPages( getDivSplitPage( totalRows, size ) );
        this.setPageSize( size );
        this.setTotal( totalRows );
    }

    public long getPages()
    {
        return pages;
    }

    public void setPages( long pages )
    {
        this.pages = pages;
    }

    public long getPageSize()
    {
        return pageSize;
    }

    public void setPageSize( long pageSize )
    {
        this.pageSize = pageSize;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal( long total )
    {
        this.total = total;
    }

    public long getPageNum()
    {
        return pageNum;
    }

    public void setPageNum( long pageNum )
    {
        this.pageNum = pageNum;
    }
    
    public static long getDivSplitPage(long paramInt1, long paramInt2)
    {
    	long i = 0;
        if ((paramInt2 > 0) && (paramInt1 >= 0))
        {
            i = paramInt1 / paramInt2;
            if (paramInt1 % paramInt2 != 0)
            {
                i++;
            }
        }
        return i;
    }

}
