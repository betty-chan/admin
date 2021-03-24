package cn.exrick.xboot.modules.base.service;

import cn.exrick.xboot.base.XbootBaseService;
import cn.exrick.xboot.modules.base.entity.Announce;

import java.util.List;

/**
 * @author chenxin
 */
public interface AnnounceService extends XbootBaseService<Announce, String> {
    List<Announce> get();
}
