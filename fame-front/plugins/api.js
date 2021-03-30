import { get, post } from './http'

const api = {
  getArticles(page, limit, sort) {
    const params = {
      page,
      limit: limit || 5,
      sort,
    }
    return get('/post', params)
  },
  getArticle(id) {
    return get('/post/' + id)
  },
  getCategories() {
    return get('/category')
  },
  getTags() {
    return get('/tag')
  },
  getHeader() {
    return get('/header')
  },
  getArchives() {
    return get('/archive')
  },
  getComment(articleId, page, limit) {
    const params = {
      articleId,
      page,
      limit: limit || 5,
    }
    return get('comment', params)
  },
  postComment(articleId, parentId, content, name, email, website) {
    const params = {
      articleId,
      parentId,
      content,
      name,
      email,
      website,
    }
    return post('/comment', params)
  },
  assessComment(commentId, assess) {
    const params = {
      assess,
    }
    return post('/comment/' + commentId + '/assess', params)
  },
  getOptions() {
    return get('/option')
  },
}

export default api
