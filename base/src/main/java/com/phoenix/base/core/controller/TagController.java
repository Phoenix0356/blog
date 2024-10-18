package com.phoenix.base.core.controller;

import com.phoenix.common.annotation.AuthorizationRequired;
import com.phoenix.common.annotation.FilterNeeded;
import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.base.core.service.TagService;
import com.phoenix.common.enumeration.Role;
import com.phoenix.common.dto.ArticleAddTagDTO;
import com.phoenix.common.dto.TagDTO;
import com.phoenix.common.vo.ResultVO;
import com.phoenix.base.model.vo.TagVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    final private TagService tagService;

    @GetMapping("/all")
    @AuthorizationRequired(Role.WRITER)
    public ResultVO getAllTagsList(){
        List<TagVO> tagVOList = tagService.getTagList();
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,tagVOList);
    }

    @GetMapping("/{articleId}")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getArticleTagsList(@PathVariable String articleId){
        List<TagVO> tagVOList = tagService.getArticleTagsList(articleId);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,tagVOList);
    }

    @PostMapping("/save")
    @AuthorizationRequired(Role.WRITER)
    @FilterNeeded
    public ResultVO saveTag(@RequestBody TagDTO tagDTO){
        tagService.saveTag(tagDTO);
        return ResultVO.success(RespMessageConstant.SAVE_SUCCESS);
    }

    @PostMapping("/article/update")
    @AuthorizationRequired(Role.WRITER)
    public ResultVO addTagToArticle(@RequestBody ArticleAddTagDTO articleAddTagDTO){
        tagService.updateTagToArticle(articleAddTagDTO);
        return ResultVO.success(RespMessageConstant.SAVE_SUCCESS);
    }

    @PutMapping("/update")
    @AuthorizationRequired(Role.WRITER)
    @FilterNeeded
    public ResultVO updateTag(@RequestBody TagDTO tagDTO){
        tagService.updateTag(tagDTO);
        return ResultVO.success(RespMessageConstant.UPDATE_SUCCESS);
    }

    @DeleteMapping("/delete/{tagId}")
    @AuthorizationRequired(Role.WRITER)
    public ResultVO deleteTagById(@PathVariable String tagId){
        tagService.deleteTagById(tagId);
        return ResultVO.success(RespMessageConstant.DELETE_SUCCESS);
    }
}
