
package com.wnc.sboot1.cluster.mq.zb8;

import org.apache.log4j.Logger;

import com.wnc.sboot1.spy.zuqiu.FunnyCommetSpy;

//@Component
//@RabbitListener( queues = "zb8_news_history_date" )
public class Zb8HistoryDateReceiver
{

//    @Autowired
    private FunnyCommetSpy funnyCmtSpy;
//    @Autowired
    Zb8HistoryDateSender zb8HistoryDateSender;

    Logger logger = Logger.getLogger( Zb8HistoryDateReceiver.class );

//    @RabbitHandler
//    public void process( String day )
//    {
//        logger.info( "Receiver object : " + day );
//        try
//        {
//            funnyCmtSpy.setSpyDay( day ).spy();
//            logger.info( " : FunnyCmt Over-" + day );
//        } catch ( Exception e )
//        {
//            e.printStackTrace();
//            logger.error( " : FunnyCmt-Err in " + day );
//            // 异常的时候重新放入mq
//            zb8HistoryDateSender.send( day );
//        }
//    }

}
