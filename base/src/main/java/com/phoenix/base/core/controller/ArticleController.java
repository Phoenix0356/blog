package com.phoenix.base.core.controller;

import com.phoenix.base.model.vo.ArticleDataVO;
import com.phoenix.common.annotation.AuthorizationRequired;
import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.base.context.TokenContext;
import com.phoenix.base.core.service.ArticleService;
import com.phoenix.common.enumeration.Role;
import com.phoenix.common.dto.ArticleDTO;
import com.phoenix.base.model.vo.ArticleVO;
import com.phoenix.common.vo.ResultVO;
import com.phoenix.common.annotation.FilterNeeded;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    final ArticleService articleService;

    @GetMapping("/visitor/{articleId}")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getArticleById(@PathVariable String articleId){
        ArticleVO articleVO = articleService.getArticleDetailById(articleId);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS, articleVO);
    }

    @GetMapping("/visitor/all")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getArticleAll(@RequestParam("sortBy") int sortStrategy){
        List<ArticleVO> articleVOList= articleService.getArticleAll(sortStrategy);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,articleVOList);
    }
    @GetMapping("/list")
    @AuthorizationRequired(Role.WRITER)
    public ResultVO getUserArticleListById(){
        List<ArticleVO> articleVOList = articleService.getArticleUserList(TokenContext.getUserId());
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,articleVOList);
    }

    @GetMapping("/data/{articleId}")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getArticleDataState(@PathVariable("articleId") String articleId){
        ArticleDataVO articleDataVO = articleService.getArticleDataStateById(articleId);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,articleDataVO);
    }

    @PostMapping("/save")
    @AuthorizationRequired(Role.WRITER)
    @FilterNeeded
    public ResultVO saveArticle(@RequestBody ArticleDTO articleDTO){
        articleDTO.setArticleUserId(TokenContext.getUserId());
        ArticleVO articleVO = articleService.saveArticleByUser(articleDTO);
        return ResultVO.success(RespMessageConstant.SAVE_SUCCESS,articleVO);
    }

    @PutMapping("/update/content")
    @AuthorizationRequired(Role.WRITER)
    @FilterNeeded
    public ResultVO updateArticleContent(@RequestBody ArticleDTO articleDTO){
        articleService.updateArticleContent(articleDTO);
        return ResultVO.success(RespMessageConstant.UPDATE_SUCCESS);
    }

    @PutMapping("/update/data")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO updateAuthorizedArticleData(@RequestBody ArticleDTO articleDTO){
        articleService.updateAuthorizedArticleData(articleDTO);
        return ResultVO.success(RespMessageConstant.UPDATE_SUCCESS);
    }

    @PutMapping("/visitor/update/{articleId}")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO updateCommonArticleData(@PathVariable("articleId") String articleId){
        articleService.updateCommonArticleData(articleId);
        return ResultVO.success(RespMessageConstant.UPDATE_SUCCESS);
    }

    @DeleteMapping("/delete/{articleId}")
    @AuthorizationRequired(Role.WRITER)
    public ResultVO deleteArticle(@PathVariable String articleId){
        articleService.deleteArticleById(articleId);
        return ResultVO.success(RespMessageConstant.DELETE_SUCCESS);
    }
}
