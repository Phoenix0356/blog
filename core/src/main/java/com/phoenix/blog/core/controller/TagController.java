package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.constant.RespMessageConstant;
import com.phoenix.blog.core.service.TagService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.dto.ArticleAddTagDTO;
import com.phoenix.blog.model.dto.TagDTO;
import com.phoenix.blog.model.vo.ResultVO;
import com.phoenix.blog.model.vo.TagVO;
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

    @GetMapping("/get/{articleId}")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getArticleTagsList(@PathVariable String articleId){
        List<TagVO> tagVOList = tagService.getArticleTagsList(articleId);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,tagVOList);
    }

    @PostMapping("/save")
    @AuthorizationRequired(Role.WRITER)
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
