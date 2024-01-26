import axios from 'axios'
import { config } from '../../Constants'

export const Api = {
  authenticate,
  signup,
  getItems,
  getItem,
  getBid,
  placeBid,
  createItemCategory,
  getItemCategories,
  deleteItemCategory,
  createItemProducer,
  getItemProducers,
  deleteItemProducer,
  addItem
}

function authenticate(username, password) {
  return instance.post('/auth/authenticate', { username, password }, {
    headers: { 'Content-type': 'application/json' }
  })
}

function signup(user) {
  return instance.post('/auth/signup', user, {
    headers: { 'Content-type': 'application/json' }
  })
}

function getItems(params) {
  const urlParams = new URLSearchParams();
  for (const [key, value] of Object.entries(params)) {
    urlParams.append(key, value);
  }
  
  return instance.get(`/items${urlParams.toString().length > 0 ? `?${urlParams.toString()}` : ""}`,  {
    headers: { 'Content-type': 'application/json' 
    }});
}

function getItem(itemId) {
  return instance.get(`/items/${itemId}`, {
    headers: { 'Content-type': 'application/json' }
  })
}

function getBid(itemId) {
  return instance.get(`/bids/${itemId}`, {
    headers: { 'Content-Type': 'application/json' }
  });
}

function placeBid(bidRequest, user) {
  return instance.post(`/bids/placeBid`, bidRequest, {
    headers: {
      'Content-type': 'application/json',
      'Authorization': basicAuth(user)
    }
  });
}

function createItemCategory(categoryName, user) {
  return instance.post(`/itemCategories`, null, {
    headers: { 
      'Content-Type': 'application/json',
      'Authorization': basicAuth(user) 
    },
    params: {
      categoryName: categoryName
    }});
}

function getItemCategories() {
  return instance.get('/itemCategories', {
    headers: { 'Content-type': 'application/json' }
  });
}

function deleteItemCategory(categoryId, user) {
  return instance.delete(`/itemCategories/${categoryId}`, {
    headers: { 
      'Content-Type': 'application/json',
      'Authorization': basicAuth(user) 
    }
  });
}

function createItemProducer(producerName, user) {
  return instance.post(`/itemProducers`, null, {
    headers: { 
      'Content-Type': 'application/json',
      'Authorization': basicAuth(user) 
    },
    params: {
      producerName: producerName
    }});
}

function getItemProducers() {
  return instance.get('/itemProducers', {
    headers: { 'Content-type': 'application/json' }
  })
}

function deleteItemProducer(producerId, user) {
  return instance.delete(`/itemProducers/${producerId}`, {
    headers: { 
      'Content-Type': 'application/json',
      'Authorization': basicAuth(user) 
    }
  });
}

function addItem(item, user) {
  return instance.post('items', item, {
    headers: { 
      'Content-type': 'application/json',
      'Authorization': basicAuth(user)
    }
  });
}

// -- Axios

const instance = axios.create({
  baseURL: config.url.API_BASE_URL
})

// -- Helper functions

function basicAuth(user) {
  return `Basic ${user.authdata}`
}