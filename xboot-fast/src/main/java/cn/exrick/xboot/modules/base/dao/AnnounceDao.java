package cn.exrick.xboot.modules.base.dao;

import cn.exrick.xboot.base.XbootBaseDao;
import cn.exrick.xboot.modules.base.entity.Announce;

import java.util.List;

public interface AnnounceDao extends XbootBaseDao<Announce,String> {
    List<Announce> findAll();
    Boolean update();
}
