package com.javamaster.service;

import com.javamaster.entity.HashtagEntity;
import com.javamaster.entity.MessageEntity;
import com.javamaster.repository.HashtagEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HashtagService {
    @Autowired
    HashtagEntityRepository hashtagEntityRepository;

    public List<HashtagEntity> findAll(){
        return hashtagEntityRepository.findAll();
    }

    public HashtagEntity findByName(String name){
        return hashtagEntityRepository.findByName(name);
    }

    @Transactional
    public void save(HashtagEntity hashtagEntity){
        hashtagEntityRepository.save(hashtagEntity);
    }

    public List<MessageEntity> getMessageByHashtagName(String name){
        HashtagEntity byName = hashtagEntityRepository.findByName(name);
        if(byName == null)
            return null;
        return byName.getMessageEntitySet()
                .stream().collect(Collectors.toList());
    }

    public Set<HashtagEntity> getSetHashtagEntity(Set<String> hashtags) {
        Set<HashtagEntity> set = new HashSet<>();
        Iterator<String> iterator = hashtags.iterator();
        while (iterator.hasNext()){
            HashtagEntity byName = this.findByName(iterator.next());
            if(byName == null){
                byName = new HashtagEntity();
                byName.setName(iterator.next());
                this.save(byName);
            }
            set.add(byName);
        }
        return set;
    }
}
