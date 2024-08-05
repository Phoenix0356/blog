package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.constant.RespMessageConstant;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.service.CollectionService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.dto.ArticleNoteDTO;
import com.phoenix.blog.model.dto.CollectionAddDTO;
import com.phoenix.blog.model.dto.CollectionDTO;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.CollectionVO;
import com.phoenix.blog.model.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
public class CollectionController {
    private final CollectionService collectionService;


    @GetMapping("/get/{collectionId}")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getCollection(@PathVariable String collectionId){
        CollectionVO collectionVO = collectionService.getCollection(collectionId);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,collectionVO);
    }

    @GetMapping("/all/{username}")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getAllCollection(@PathVariable String username){
        List<CollectionVO> collectionVOList = collectionService.getAllCollections(username);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,collectionVOList);
    }

    @GetMapping("/{collectionId}/articles")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getAllArticles(@PathVariable String collectionId){
        List<ArticleVO> allArticleList = collectionService.getAllArticleList(collectionId);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,allArticleList);
    }

    //添加文章
    @PostMapping("/add")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO saveArticleIntoCollection(@RequestBody CollectionAddDTO collectionAddDTO){
        collectionService.saveArticleIntoCollection(collectionAddDTO,TokenContext.getUserId());
        return ResultVO.success(RespMessageConstant.SAVE_SUCCESS);
    }

    @PostMapping("/{collectionId}/article/remark")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO saveRemarkIntoArticle(@PathVariable String collectionId,
                                          @RequestBody ArticleNoteDTO articleRemarkDTO){
        collectionService.saveNoteIntoArticle(collectionId,articleRemarkDTO);
        return ResultVO.success(RespMessageConstant.SAVE_SUCCESS);
    }

    //创建收藏夹
    @PostMapping("/save")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO saveCollection(@RequestBody CollectionDTO collectionDTO){
        collectionService.saveCollection(collectionDTO,TokenContext.getUserId());
        return ResultVO.success(RespMessageConstant.SAVE_SUCCESS);
    }

    @PutMapping("/update")
    public ResultVO updateCollection(@RequestBody CollectionDTO collectionDTO){
        collectionService.updateCollection(collectionDTO);
        return ResultVO.success(RespMessageConstant.UPDATE_SUCCESS);
    }

    @DeleteMapping("/delete/{collectionId}/{articleId}")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO deleteArticleFromCollection(@PathVariable String collectionId,
                                                @PathVariable String articleId){
        collectionService.deleteArticleFromCollect(collectionId,articleId);
        return ResultVO.success(RespMessageConstant.DELETE_SUCCESS);
    }
    @DeleteMapping("/delete/{collectionId}")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO deleteCollection(@PathVariable String collectionId){
        collectionService.deleteCollectionById(collectionId);
        return ResultVO.success(RespMessageConstant.DELETE_SUCCESS);
    }
}
