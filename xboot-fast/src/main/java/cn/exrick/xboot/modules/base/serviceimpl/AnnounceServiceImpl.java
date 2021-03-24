package cn.exrick.xboot.modules.base.serviceimpl;

import cn.exrick.xboot.modules.base.dao.AnnounceDao;
import cn.exrick.xboot.modules.base.service.AnnounceService;
import cn.exrick.xboot.modules.base.entity.Announce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author chenxin
 */
@Service
@Transactional
public class AnnounceServiceImpl implements AnnounceService {
    @Autowired
    private AnnounceDao announceDao;

    @Override
    public List<Announce> get() {
        return announceDao.findAll();
    }
}
