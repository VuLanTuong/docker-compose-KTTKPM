const moreBtn = document.querySelector('button.read-more');
const postContent = document.querySelector('.post-content');

let isReadMore = true;

moreBtn.addEventListener('click', () => {
    isReadMore = !isReadMore;
    moreBtn.innerText = isReadMore ? 'Read more' : 'Read less';
    postContent.classList[isReadMore ? 'add' : 'remove']('line-clamp-5');
});